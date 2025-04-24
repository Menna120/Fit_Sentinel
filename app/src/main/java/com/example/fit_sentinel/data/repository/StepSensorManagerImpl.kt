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

    private var initialHardwareSteps: Float? = null

    private var accelerometerStepCount = 0

    // Timestamp of the last detected step in nanoseconds.
    private var lastStepTimeNs: Long = 0

    // Threshold for accelerometer magnitude to detect a potential step peak.
    private val magnitudeThreshold = 11.5f

    // Minimum time required between steps in nanoseconds to filter out noise/vibration.
    private val stepTimeNsThreshold: Long = 250_000_000

    // Alpha for the low-pass filter on accelerometer magnitude.
    private val filterAlpha = 0.8f // Value between 0 and 1. Higher alpha = more smoothing.

    // Stores the last filtered magnitude value.
    private var lastMagnitude = 0f

    private var lastGyroEvent: SensorEvent? = null

    init {
        determineSensorMode()
    }

    private fun determineSensorMode() {
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepCounterSensor != null) {
            _currentMode.value = SensorMode.HARDWARE
            Log.i("StepSensorManager", "Using Hardware Step Counter sensor.")
        } else {
            Log.w("StepSensorManager", "Hardware Step Counter sensor not available.")
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            if (accelerometerSensor != null) {
                _currentMode.value = SensorMode.ACCELEROMETER
                if (gyroscopeSensor != null) {
                    Log.i("StepSensorManager", "Falling back to Accelerometer + Gyroscope sensors.")
                } else {
                    Log.w(
                        "StepSensorManager",
                        "Falling back to Accelerometer sensor (Gyroscope not available). Accuracy may be lower."
                    )
                }
            } else {
                _currentMode.value = SensorMode.UNAVAILABLE
                Log.e(
                    "StepSensorManager",
                    "Critical: Neither Step Counter nor Accelerometer sensor found! Step tracking unavailable."
                )
            }
        }
    }

    override fun startListening() {
        if (!isSensorAvailable) {
            Log.w("StepSensorManager", "Cannot start listening, no suitable sensor.")
            return
        }

        _sessionSteps.value = 0

        when (_currentMode.value) {
            SensorMode.HARDWARE -> {
                initialHardwareSteps = null
                val registered = sensorManager.registerListener(
                    this,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                Log.d("StepSensorManager", "Registered HARDWARE listener: $registered")
            }

            SensorMode.ACCELEROMETER -> {
                accelerometerStepCount = 0
                lastStepTimeNs = 0
                lastMagnitude = 0f
                lastGyroEvent = null

                val registeredAccel = sensorManager.registerListener(
                    this,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME
                )
                Log.d("StepSensorManager", "Registered ACCELEROMETER listener: $registeredAccel")

                if (gyroscopeSensor != null) {
                    val registeredGyro = sensorManager.registerListener(
                        this,
                        gyroscopeSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                    Log.d("StepSensorManager", "Registered GYROSCOPE listener: $registeredGyro")
                }

                Log.w(
                    "StepSensorManager",
                    "Accelerometer mode active: Battery consumption will be higher than hardware mode."
                )
            }

            SensorMode.UNAVAILABLE -> {
                Log.w("StepSensorManager", "Attempted to start listening in UNAVAILABLE mode.")
            }
        }
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this)
        Log.d("StepSensorManager", "Unregistered sensor listeners.")
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

    private fun handleHardwareStepEvent(event: SensorEvent) {
        val currentRawSteps = event.values[0]

        if (initialHardwareSteps == null) {
            initialHardwareSteps = currentRawSteps
            _sessionSteps.value = 0
            Log.d("StepSensorManager", "HARDWARE Initialized. Baseline: $initialHardwareSteps")
        } else {
            val stepsTaken = (currentRawSteps - (initialHardwareSteps ?: currentRawSteps)).toInt()

            if (stepsTaken >= _sessionSteps.value) {
                if (stepsTaken < 0) {
                    Log.w(
                        "StepSensorManager",
                        "HARDWARE step counter reset detected. Re-initializing baseline."
                    )
                    initialHardwareSteps = currentRawSteps
                    _sessionSteps.value = 0
                } else {
                    _sessionSteps.value = stepsTaken
                }
            } else if (currentRawSteps < (initialHardwareSteps ?: 0f)) {
                Log.w(
                    "StepSensorManager",
                    "HARDWARE step counter reset detected (raw value below baseline). Re-initializing."
                )
                initialHardwareSteps = currentRawSteps
                _sessionSteps.value = 0
            }
            Log.d(
                "StepSensorManager",
                "Raw: $currentRawSteps, Baseline: $initialHardwareSteps, Session: ${_sessionSteps.value}"
            )
        }
    }

    private fun handleAccelerometerEvent(event: SensorEvent) {
        val currentTimeNs = event.timestamp

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        var magnitude = sqrt(x * x + y * y + z * z)

        magnitude = filterAlpha * lastMagnitude + (1 - filterAlpha) * magnitude
        lastMagnitude = magnitude

        val isPeakCandidate = magnitude > magnitudeThreshold
        val isEnoughTimePassed = (currentTimeNs - lastStepTimeNs > stepTimeNsThreshold)

        val isLikelyWalking = isLikelyWalkingBasedOnGyro()


        if (isPeakCandidate && isEnoughTimePassed) {
            accelerometerStepCount++
            _sessionSteps.value =
                accelerometerStepCount
            lastStepTimeNs = currentTimeNs

            Log.d(
                "StepSensorManager",
                "ACCEL Step Detected! Count: $accelerometerStepCount, Mag: $magnitude, Gyro Validation: $isLikelyWalking"
            )
        }
    }

    private fun handleGyroscopeEvent(event: SensorEvent) {
        lastGyroEvent = event
//        Log.d(
//            "StepSensorManager",
//            "GYRO Event: x=${event.values[0]}, y=${event.values[1]}, z=${event.values[2]}"
//        )
    }

    private fun isLikelyWalkingBasedOnGyro(): Boolean {
        // TODO: Implement sophisticated logic here.
        // Analyze lastGyroEvent (angular velocity).
        // Typical walking involves rhythmic rotations around certain axes.
        // You might look for:
        // - Peaks in angular velocity that correlate with accelerometer peaks.
        // - Specific patterns or ranges of angular velocity.
        // - State machine based on accelerometer and gyroscope patterns.

        // Example (very basic, not accurate): Check if there's *any* significant rotation
        val gyroEvent =
            lastGyroEvent ?: return true // If no gyro data, assume walking (less accurate)

        val gyroX = gyroEvent.values[0]
        val gyroY = gyroEvent.values[1]
        val gyroZ = gyroEvent.values[2]

        val angularVelocityMagnitude = sqrt(gyroX * gyroX + gyroY * gyroY + gyroZ * gyroZ)

        // Define example thresholds based on experimentation.
        // A more robust algorithm would analyze patterns over time.
        val isRotating = angularVelocityMagnitude > 0.5 // Example: Check if there's some rotation

        return isRotating // This is overly simplistic; needs real algorithm
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
}