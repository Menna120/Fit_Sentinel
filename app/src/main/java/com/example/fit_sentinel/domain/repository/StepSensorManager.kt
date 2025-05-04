package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.model.SensorMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StepSensorManager {
    val currentSteps: StateFlow<Int>
    val isSensorAvailable: Boolean
    val sensorMode: Flow<SensorMode>
    fun startListening()
    fun stopListening()
    fun resetSensors()
}