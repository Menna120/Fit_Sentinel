package com.example.fit_sentinel.data.repository

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.domain.repository.StepSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class StepSensorManagerImpl @Inject constructor(
    private val sensorManager: SensorManager
) : StepSensorManager, SensorEventListener {

    private var stepCounterSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null

    private val _currentMode = MutableStateFlow(SensorMode.UNAVAILABLE)
    override val sensorMode: StateFlow<SensorMode> = _currentMode

    private val _sessionSteps = MutableStateFlow(0)
    override val currentSteps: StateFlow<Int> = _sessionSteps

    override val isSensorAvailable: Boolean
        get() = _currentMode.value != SensorMode.UNAVAILABLE

    // --- Hardware Step Counter State ---
    private var initialHardwareSteps: Float? = null

    // --- Accelerometer + Gyroscope Step Detector State ---
    // Alpha for the complementary filter (combining gyro and accel orientation)
    // A value closer to 1 trusts the gyro integration more, closer to 0 trusts accel more.
    // Tuning is crucial: 0.98 is a common starting point, but can vary.
    private val complementaryFilterAlpha = 0.98f // Increased trust in gyroscope

    // Step detection parameters (using a dynamic threshold)
    private var dynamicThresholdOffset =
        2f // Offset above recent minimum for dynamic threshold (m/s^2) - Needs tuning
    private val minStepIntervalNs: Long =
        250_000_000 // Minimum time between steps in nanoseconds (250ms)

    private var accelLastStepTimeNs = 0L // Timestamp of the last detected step (ns)
    private var currentAccelStepCount = 0 // Steps detected by accelerometer in the current session

    // Orientation state for gravity removal / vertical acceleration calculation
    private var roll: Float = 0f
    private var pitch: Float = 0f
    private var yaw: Float =
        0f // Adding yaw for completeness, though roll/pitch are primary for vertical

    // Store the last gyroscope event for timestamp and values
    private var lastGyroEvent: SensorEvent? = null

    // Timestamp of the last sensor fusion update (used for calculating delta time)
    private var lastSensorFusionTimestampNs: Long = 0L

    private var isGyroAvailableForAccel =
        false // Flag to indicate if gyro is available for orientation correction

    // Moving Average filter parameters for vertical acceleration
    private val windowSize = 20 // Size of the moving average window
    private val movingAverageQueue = ArrayDeque<Float>() // Queue for moving average

    // Variables for dynamic threshold calculation (now based on moving average)
    private var minMovingAverageVerticalAccel =
        0f // Track recent minimum moving average vertical acceleration
    private val thresholdWindowNs: Long =
        1_000_000_000 // Window for tracking min/max in nanoseconds (1 second)
    private var lastThresholdUpdateTimeNs: Long = 0L


    init {
        determineSensorMode()
        // Sensor listeners are registered and unregistered in startListening/stopListening
    }

    private fun determineSensorMode() {
//        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        if (stepCounterSensor != null) {
//            _currentMode.value = SensorMode.HARDWARE
//            Log.i("StepSensorManager", "Using Hardware Step Counter sensor.")
//        } else {
        Log.w("StepSensorManager", "Hardware Step Counter sensor not available.")
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (accelerometerSensor != null) {
            _currentMode.value = SensorMode.ACCELEROMETER
            isGyroAvailableForAccel = gyroscopeSensor != null // Set flag
            if (isGyroAvailableForAccel) {
                Log.i("StepSensorManager", "Falling back to Accelerometer + Gyroscope sensors.")
            } else {
                Log.w(
                    "StepSensorManager",
                    "Falling back to Accelerometer sensor (Gyroscope not available). Accuracy may be lower."
                )
                // Note: Step detection without gyro orientation correction is highly susceptible to device tilt.
                // The verticalAccelThreshold will need careful tuning or a different approach might be needed.
            }
        } else {
            _currentMode.value = SensorMode.UNAVAILABLE
            Log.e(
                "StepSensorManager",
                "Critical: Neither Step Counter nor Accelerometer sensor found! Step tracking unavailable."
            )
        }
//        }
    }

    override fun startListening() {
        if (!isSensorAvailable) {
            Log.w("StepSensorManager", "Cannot start listening, no suitable sensor.")
            return
        }

        // Reset session steps and detector state based on the mode
        _sessionSteps.value = 0

        when (_currentMode.value) {
            SensorMode.HARDWARE -> {
                initialHardwareSteps = null // Reset baseline for new session
                val registered = sensorManager.registerListener(
                    this,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_FASTEST
                )
                Log.d("StepSensorManager", "Registered HARDWARE listener: $registered")
            }

            SensorMode.ACCELEROMETER -> {
                // Reset accelerometer detector state for new session
                currentAccelStepCount = 0
                accelLastStepTimeNs = 0L
                // Reset orientation state
                roll = 0f
                pitch = 0f
                yaw = 0f
                lastGyroEvent = null // Reset last gyro event
                lastSensorFusionTimestampNs = 0L // Reset fusion timestamp
                // Reset moving average and dynamic threshold variables
                movingAverageQueue.clear()
                minMovingAverageVerticalAccel = 0f
                lastThresholdUpdateTimeNs = 0L


                val registeredAccel = sensorManager.registerListener(
                    this,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME // SENSOR_DELAY_GAME provides faster data for detection
                )
                Log.d("StepSensorManager", "Registered ACCELEROMETER listener: $registeredAccel")

                if (isGyroAvailableForAccel) {
                    val registeredGyro = sensorManager.registerListener(
                        this,
                        gyroscopeSensor,
                        SensorManager.SENSOR_DELAY_GAME // Match accelerometer delay
                    )
                    Log.d(
                        "StepSensorManager",
                        "Registered GYROSCOPE listener: $registeredGyro"
                    )
                }
                Log.w(
                    "StepSensorManager",
                    "Accelerometer mode active: Battery consumption will be higher than hardware mode."
                )
                Log.i(
                    "StepSensorManager",
                    "Accelerometer detector using complementary filter, vertical acceleration, and moving average"
                )
            }

            SensorMode.UNAVAILABLE -> {
                Log.w("StepSensorManager", "Attempted to start listening in UNAVAABLE mode.")
            }
        }
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("StepSensorManager", "Unregistered sensor listeners.")
    }

    override fun resetSensors() {
        if (_currentMode.value == SensorMode.HARDWARE)
            initialHardwareSteps = _sessionSteps.value.toFloat()
        _sessionSteps.value = 0
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                if (_currentMode.value == SensorMode.HARDWARE) {
                    handleHardwareStepEvent(event)
                }
            }

            Sensor.TYPE_ACCELEROMETER -> {
                if (_currentMode.value == SensorMode.ACCELEROMETER) {
                    handleAccelerometerEvent(event)
                }
            }

            Sensor.TYPE_GYROSCOPE -> {
                if (_currentMode.value == SensorMode.ACCELEROMETER && gyroscopeSensor != null) {
                    handleGyroscopeEvent(event)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val sensorName = when (sensor?.type) {
            Sensor.TYPE_STEP_COUNTER -> "Hardware Step Counter"
            Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
            Sensor.TYPE_GYROSCOPE -> "Gyroscope"
            else -> "Unknown Sensor (${sensor?.type})"
        }
        val accuracyLevel = when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "High"
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Medium"
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "Low"
            SensorManager.SENSOR_STATUS_UNRELIABLE -> "Unreliable"
            else -> "Unknown ($accuracy)"
        }
        Log.i("StepSensorManager", "Accuracy changed for $sensorName: $accuracyLevel")
    }

    // --- Hardware Step Counter Handler ---
    private fun handleHardwareStepEvent(event: SensorEvent) {
        val currentRawSteps = event.values[0]

        if (initialHardwareSteps == null) {
            initialHardwareSteps = currentRawSteps
            _sessionSteps.value = 0 // Start session count from 0
            Log.d("StepSensorManager", "HARDWARE Initialized. Baseline: $initialHardwareSteps")
        } else {
            // Calculate steps taken since the start of the session
            val stepsTaken =
                (currentRawSteps - initialHardwareSteps!!).toInt()

            // Handle potential sensor reset or wrap-around
            if (stepsTaken < 0) {
                Log.w(
                    "StepSensorManager",
                    "HARDWARE step counter reset or wrap-around detected. Re-initializing baseline."
                )
                initialHardwareSteps = currentRawSteps // Reset baseline to current raw value
                _sessionSteps.value = 0 // Reset session steps
            } else {
                // Update session steps if the new count is higher (normal operation)
                _sessionSteps.value = stepsTaken
            }

            Log.d(
                "StepSensorManager",
                "HARDWARE Event. Raw: $currentRawSteps, Baseline: $initialHardwareSteps, Session: ${_sessionSteps.value}"
            )
        }
    }


    // --- Accelerometer + Gyroscope Step Detector Handlers ---

    private fun handleGyroscopeEvent(event: SensorEvent) {
        // Store the entire gyroscope event for timestamp and values
        lastGyroEvent = event
    }

    private fun handleAccelerometerEvent(event: SensorEvent) {
        val currentTimeNs = event.timestamp
        val accelValues = event.values

        // Calculate orientation from accelerometer data (absolute but noisy)
        val (rollAcc, pitchAcc, yawAcc) = calculateOrientationFromAccel(accelValues)

        // Calculate time delta since the last sensor fusion step.
        // Use current time if it's the first event in the session.
        val lastFusionTimestamp =
            if (lastSensorFusionTimestampNs == 0L) currentTimeNs else lastSensorFusionTimestampNs
        val deltaTimeNs = currentTimeNs - lastFusionTimestamp
        val deltaTimeSeconds = deltaTimeNs / 1_000_000_000.0f // Delta time in seconds

        // Apply complementary filter to combine accelerometer and gyroscope orientation
        if (isGyroAvailableForAccel && lastGyroEvent != null && lastSensorFusionTimestampNs != 0L) { // Ensure gyro sensor is available and data has been received after first fusion step
            val gyroValues = lastGyroEvent!!.values // Use the values from the last gyro event

            // Integrate gyroscope data to estimate change in orientation (in degrees)
            // Angular velocity (rad/s) * dt (s) = change in angle (rad)
            // Convert rad to degrees for roll/pitch/yaw state
            val deltaRollGyro =
                (gyroValues[1] * deltaTimeSeconds).toDegrees() // Rotation around Y-axis affects roll
            val deltaPitchGyro =
                (gyroValues[0] * deltaTimeSeconds).toDegrees() // Rotation around X-axis affects pitch
            val deltaYawGyro =
                (gyroValues[2] * deltaTimeSeconds).toDegrees() // Rotation around Z-axis affects yaw


            // Apply complementary filter:
            // New Estimate = alpha * (Gyro Integrated Estimate) + (1 - alpha) * (Accelerometer Estimate)
            // Gyro Integrated Estimate = Previous Estimate + Delta from Gyro
            roll =
                complementaryFilterAlpha * (roll + deltaRollGyro) + (1 - complementaryFilterAlpha) * rollAcc
            pitch =
                complementaryFilterAlpha * (pitch + deltaPitchGyro) + (1 - complementaryFilterAlpha) * pitchAcc
            yaw =
                complementaryFilterAlpha * (yaw + deltaYawGyro) + (1 - complementaryFilterAlpha) * yawAcc

            // Normalize angles to be within a standard range (e.g., -180 to 180)
            roll = normalizeDegree(roll)
            pitch = normalizeDegree(pitch)
            yaw = normalizeDegree(yaw)

        } else {
            // If gyro is not available or no gyro data received yet (before the first fusion step),
            // just use accelerometer orientation (less accurate and noisy).
            // This effectively becomes a low-pass filter on accelerometer data if complementaryFilterAlpha < 1.
            // The very first orientation estimate will rely solely on accelerometer.
            if (lastSensorFusionTimestampNs == 0L) {
                roll = rollAcc
                pitch = pitchAcc
                yaw = yawAcc // Yaw from accel is unreliable
            } else {
                roll =
                    complementaryFilterAlpha * roll + (1 - complementaryFilterAlpha) * rollAcc
                pitch =
                    complementaryFilterAlpha * pitch + (1 - complementaryFilterAlpha) * pitchAcc
                yaw =
                    complementaryFilterAlpha * yaw + (1 - complementaryFilterAlpha) * yawAcc // Yaw from accel is unreliable
            }
            // Normalize angles
            roll = normalizeDegree(roll)
            pitch = normalizeDegree(pitch)
            yaw = normalizeDegree(yaw) // Yaw from accel is very noisy/unreliable
        }

        // Now, use the estimated orientation to get the vertical acceleration component
        // The vertical component of acceleration in the world frame corresponds to the
        // acceleration along the direction of gravity.
        val verticalAccel = getVerticalAcceleration(accelValues, roll, pitch)

        // --- Apply Moving Average Filter ---
        val movingAverageVerticalAccel = applyMovingAverage(verticalAccel)
        // ---------------------------------

        // --- Dynamic Threshold Calculation (now based on moving average) ---
        // Update the minimum moving average vertical acceleration within the current window
        if (lastThresholdUpdateTimeNs == 0L) {
            lastThresholdUpdateTimeNs = currentTimeNs
            minMovingAverageVerticalAccel =
                movingAverageVerticalAccel // Initialize min on the first data point
        }

        // If the window has passed, reset the min and start a new window
        if (currentTimeNs - lastThresholdUpdateTimeNs > thresholdWindowNs) {
            minMovingAverageVerticalAccel =
                movingAverageVerticalAccel // Start new window min from current value
            lastThresholdUpdateTimeNs = currentTimeNs
        } else {
            // Otherwise, update the minimum if the current value is lower
            if (movingAverageVerticalAccel < minMovingAverageVerticalAccel) {
                minMovingAverageVerticalAccel = movingAverageVerticalAccel
            }
        }

        // Calculate the current dynamic threshold based on the recent minimum
        val currentDynamicThreshold = minMovingAverageVerticalAccel + dynamicThresholdOffset
        // Log the current threshold for debugging
        Log.d(
            "StepSensorManager",
            "Moving Avg Accel: ${
                String.format(
                    "%.2f",
                    movingAverageVerticalAccel
                )
            }, Threshold: ${String.format("%.2f", currentDynamicThreshold)}"
        )
        // -------------------------------------

        // Use the moving average vertical acceleration and dynamic threshold for step detection (peak detection)
        detectStep(movingAverageVerticalAccel, currentDynamicThreshold, currentTimeNs)

        // Update the last fusion timestamp to the current accelerometer event's timestamp.
        // This ensures the delta time calculation is accurate for the next event.
        // Note: This assumes accelerometer events drive the fusion loop.
        // If you have higher rate gyro data, you might want the fusion to be driven by gyro events instead.
        lastSensorFusionTimestampNs = currentTimeNs
    }

    // Calculate orientation (roll, pitch, yaw) from accelerometer data
    // Note: Yaw from accelerometer alone is not reliable as accel only provides
    // gravity reference, not heading.
    private fun calculateOrientationFromAccel(accel: FloatArray): Triple<Float, Float, Float> {
        val ax = accel[0]
        val ay = accel[1]
        val az = accel[2]

        // Calculate roll and pitch from accelerometer data based on gravity vector
        // These are absolute orientation relative to gravity but noisy.
        val rollAcc = atan2(ay, az).toDegrees() // Angle around X-axis
        val pitchAcc = atan2(-ax, sqrt(ay * ay + az * az)).toDegrees() // Angle around Y-axis

        // Yaw from accelerometer is unreliable without a magnetic field sensor.
        // We'll calculate a placeholder yaw, but it won't be accurate for heading.
        val yawAcc = 0f // Placeholder, accel alone cannot determine yaw reliably

        return Triple(rollAcc, pitchAcc, yawAcc)
    }

    // Get the vertical component of acceleration in the world frame
    // This is done by rotating the accelerometer vector using the estimated orientation
    // and extracting the component along the gravity vector direction.
    private fun getVerticalAcceleration(accel: FloatArray, roll: Float, pitch: Float): Float {
        // Create a rotation matrix components from roll and pitch
        val cr = cos(roll.toRadians()).toFloat()
        val sr = sin(roll.toRadians()).toFloat()
        val cp = cos(pitch.toRadians()).toFloat()
        val sp = sin(pitch.toRadians()).toFloat()

        // The vertical acceleration in the world frame is the dot product of the
        // accelerometer vector [ax, ay, az] and the world's vertical axis in the body frame.
        // Assuming world Z is vertical and points upwards, the world Z axis in body frame
        // coordinates is given by the third column of the body-to-world rotation matrix.
        // R_body_to_world = R_z(yaw) * R_y(pitch) * R_x(roll)
        // The third column (representing world Z in body frame) is [-sin(pitch), sin(roll)cos(pitch), cos(roll)cos(pitch)]
        // Since yaw doesn't affect the vertical component relative to gravity, we can ignore it here.
        // World Z in Body (Roll/Pitch only) = [-sp, sr*cp, cr*cp]

        val ax = accel[0]
        val ay = accel[1]
        val az = accel[2]

        // Calculate the vertical component of acceleration in the world frame
        // Vertical Accel = ax * (-sp) + ay * (sr*cp) + az * (cr*cp)
        val verticalAccelWorld = ax * (-sp) + ay * (sr * cp) + az * (cr * cp)

        return verticalAccelWorld
    }

    // Apply Moving Average filter
    private fun applyMovingAverage(currentValue: Float): Float {
        movingAverageQueue.add(currentValue)
        if (movingAverageQueue.size > windowSize) {
            movingAverageQueue.removeFirst()
        }
        return if (movingAverageQueue.isNotEmpty()) {
            movingAverageQueue.average().toFloat()
        } else {
            0f // Return 0 if the queue is empty
        }
    }


    // Step detection using the moving average vertical acceleration and a dynamic threshold
    private fun detectStep(verticalAccel: Float, currentThreshold: Float, currentTimeNs: Long) {
        // Step detection logic based on positive peaks in vertical acceleration above a dynamic threshold:
        // 1. Check if the moving average vertical acceleration exceeds the current dynamic threshold (potential peak).
        // 2. Ensure enough time has passed since the last detected step to avoid double counting.
        // You might also want to consider a minimum negative trough before a positive peak
        // for more robust detection (zero-crossing approach), but simple peak detection is a start.

        // Note: The 'isValidStep' peak detection from the previous StepDetector (current > last)
        // is implicitly handled here by looking for the value crossing the dynamic threshold.
        // If you strictly need 'current > last' *after* crossing the threshold, you would
        // need to track the last moving average value as well. Let's stick to threshold crossing for simplicity.

        if (verticalAccel > currentThreshold && (currentTimeNs - accelLastStepTimeNs) > minStepIntervalNs) {
            // Potential step detected
            currentAccelStepCount++ // Increment accelerometer-based step count
            _sessionSteps.value = currentAccelStepCount // Update the session steps flow
            accelLastStepTimeNs = currentTimeNs // Record the time of this step

            Log.d(
                "StepSensorManager",
                "ACCEL Step Detected! Count: $currentAccelStepCount, Moving Avg Accel: ${
                    String.format(
                        "%.2f",
                        verticalAccel
                    )
                }, Threshold: ${String.format("%.2f", currentThreshold)}"
            )

            // After detecting a step, adjust the min tracking for the dynamic threshold.
            // Resetting the min to the current peak value is a common approach.
            minMovingAverageVerticalAccel =
                verticalAccel // Start tracking new minimum from the peak
            lastThresholdUpdateTimeNs =
                currentTimeNs // Also reset the window time for the next minimum tracking cycle
        }
        // Optional: Add logic to handle the trough after a peak for more robust detection.
        // This could involve tracking when the vertical acceleration drops below a certain level
        // after exceeding the threshold, indicating the end of a step cycle.
    }

    // Helper function to normalize an angle to be within -180 to 180 degrees
    private fun normalizeDegree(angle: Float): Float {
        var normalized = angle % 360
        if (normalized > 180) {
            normalized -= 360
        } else if (normalized < -180) {
            normalized += 360
        }
        return normalized
    }

    // Extension functions for angle conversions from Float
    private fun Float.toDegrees() = Math.toDegrees(this.toDouble()).toFloat()

    private fun Float.toRadians() = Math.toRadians(this.toDouble()).toFloat()
}

