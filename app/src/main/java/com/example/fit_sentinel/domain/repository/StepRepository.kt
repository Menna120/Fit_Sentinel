package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.data.model.SensorMode
import kotlinx.coroutines.flow.Flow

interface StepRepository {
    val stepsFromSensor: Flow<Int>
    val isSensorAvailable: Boolean
    val sensorMode: Flow<SensorMode>
    fun getCurrentSessionStepsFlow(): Flow<Int>
    suspend fun getTodaySavedSteps(): Int
    suspend fun saveSteps(steps: Int)
    fun getStepHistory(): Flow<List<DailyStepsEntity>>
    fun startStepTracking()
    fun stopStepTracking()
}