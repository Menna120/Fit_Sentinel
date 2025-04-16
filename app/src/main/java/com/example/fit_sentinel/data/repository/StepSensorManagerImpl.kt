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
    private var lastStepTimeNs: Long = 0
    private val magnitudeThreshold = 11.5f
    private val stepTimeNsThreshold: Long = 250_000_000
    private val filterAlpha = 0.8f
    private var lastMagnitude = 0f

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
                Log.i("StepSensorManager", "Falling back to Accelerometer sensor.")
            } else {
                _currentMode.value = SensorMode.UNAVAILABLE
                Log.e(
                    "StepSensorManager",
                    "Critical: Neither Step Counter nor Accelerometer sensor found!"
                )
            }
        }
    }

    override fun startListening() {
        if (!isSensorAvailable) {
            Log.w("StepSensorManager", "Cannot start listening, no suitable sensor.")
            return
        }

        // Reset session steps regardless of mode
        _sessionSteps.value = 0

        when (_currentMode.value) {
            SensorMode.HARDWARE -> {
                initialHardwareSteps = null // Reset baseline for hardware counter
                val registered = sensorManager.registerListener(
                    this,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_NORMAL // Lower power for hardware counter
                )
                Log.d("StepSensorManager", "Registered HARDWARE listener: $registered")
            }

            SensorMode.ACCELEROMETER -> {
                accelerometerStepCount = 0 // Reset algorithm count
                lastStepTimeNs = 0
                lastMagnitude = 0f // Reset filter state
                // Use a faster delay for accelerometer to catch step peaks reliably
                val registered = sensorManager.registerListener(
                    this,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME // Or SENSOR_DELAY_UI. Test for balance.
                )
                // Register Gyroscope if using it
                 if (gyroscopeSensor != null) sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME)
                Log.d("StepSensorManager", "Registered ACCELEROMETER listener: $registered")
                Log.w(
                    "StepSensorManager",
                    "Accelerometer mode active: Battery consumption will be higher."
                )
            }

            SensorMode.UNAVAILABLE -> {} // Should not happen if isSensorAvailable is checked
        }
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this) // Unregisters all listeners for this instance
        Log.d("StepSensorManager", "Unregistered sensor listeners.")
        // Optionally reset internal states if desired when stopped
        // initialHardwareSteps = null
        // accelerometerStepCount = 0
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (_currentMode.value) {
            SensorMode.HARDWARE -> {
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    handleHardwareStepEvent(event)
                }
            }

            SensorMode.ACCELEROMETER -> {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    handleAccelerometerEvent(event)
                }
                 else if (event.sensor.type == Sensor.TYPE_GYROSCOPE && gyroscopeSensor != null) {
                     handleGyroscopeEvent(event) // If using gyro
                 }
            }

            SensorMode.UNAVAILABLE -> {
                // Should not receive events if unavailable, but good practice to handle
            }
        }
    }

    // --- Event Handlers ---

    private fun handleHardwareStepEvent(event: SensorEvent) {
        val currentRawSteps = event.values[0]
        if (initialHardwareSteps == null) {
            // First event after starting, set the baseline
            initialHardwareSteps = currentRawSteps
            _sessionSteps.value = 0
            Log.d("StepSensorManager", "HARDWARE Initialized. Baseline: $initialHardwareSteps")
        } else {
            // Calculate steps taken since baseline
            val stepsTaken = (currentRawSteps - (initialHardwareSteps ?: currentRawSteps)).toInt()
            if (stepsTaken >= _sessionSteps.value) { // Ensure non-decreasing count (handles potential small fluctuations)
                if (stepsTaken < 0) { // Indicates a potential sensor reset (e.g., reboot)
                    Log.w(
                        "StepSensorManager",
                        "HARDWARE step counter reset detected. Re-initializing."
                    )
                    initialHardwareSteps = currentRawSteps
                    _sessionSteps.value = 0
                } else {
                    _sessionSteps.value = stepsTaken
                }
            } else if (currentRawSteps < (initialHardwareSteps ?: 0f)) {
                // If raw value drops below initial baseline, likely a reset
                Log.w(
                    "StepSensorManager",
                    "HARDWARE step counter reset detected (below baseline). Re-initializing."
                )
                initialHardwareSteps = currentRawSteps
                _sessionSteps.value = 0
            }
            // Log.d("StepSensorManager", "Raw: $currentRawSteps, Baseline: $initialHardwareSteps, Session: ${_sessionSteps.value}")
        }
    }

    private fun handleAccelerometerEvent(event: SensorEvent) {
        val currentTimeNs = event.timestamp

        // 1. Calculate magnitude
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        var magnitude = sqrt(x * x + y * y + z * z)

        // 2. Apply simple low-pass filter (optional but recommended)
        magnitude = filterAlpha * lastMagnitude + (1 - filterAlpha) * magnitude
        lastMagnitude = magnitude

        // 3. Peak Detection Logic (Simplified)
        // Check if magnitude crosses threshold AND enough time has passed
        // This simple check might need refinement (e.g., checking for peak *shape*)
        if (magnitude > magnitudeThreshold && (currentTimeNs - lastStepTimeNs > stepTimeNsThreshold)) {
            // Optional: Add gyroscope check here to filter non-walking movements if implemented
            // if (isLikelyWalkingBasedOnGyro()) { ... }

            accelerometerStepCount++
            _sessionSteps.value = accelerometerStepCount // Update the public flow
            lastStepTimeNs = currentTimeNs
             Log.d("StepSensorManager", "ACCEL Step Detected! Count: $accelerometerStepCount, Mag: $magnitude")
        }
    }

    // Placeholder for potential Gyroscope processing
     private fun handleGyroscopeEvent(event: SensorEvent) {
         // Analyze event.values[0], event.values[1], event.values[2] (angular velocity)
         // Use this data to potentially validate if accelerometer peaks correspond to walking motion
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
        // You might want to notify the user if accuracy becomes unreliable, especially for accelerometer
    }
}