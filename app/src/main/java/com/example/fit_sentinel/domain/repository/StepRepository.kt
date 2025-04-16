package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.local.entity.StepDataEntity
import com.example.fit_sentinel.data.remote.dto.AnalysisRequest
import com.example.fit_sentinel.data.remote.dto.AnalysisResponse
import com.example.fit_sentinel.data.model.SensorMode
import kotlinx.coroutines.flow.Flow

interface StepRepository {
    val stepsFromSensor: Flow<Int>
    val isSensorAvailable: Boolean
    val sensorMode: Flow<SensorMode>
    suspend fun getTodaysTotalSteps(): Int
    suspend fun saveSteps(steps: Int)
    fun getStepHistory(): Flow<List<StepDataEntity>>
    suspend fun getStepAnalysis(request: AnalysisRequest): Result<AnalysisResponse>
    fun startStepTracking()
    fun stopStepTracking()
}