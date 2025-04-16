package com.example.fit_sentinel.data.repository

import android.util.Log
import com.example.fit_sentinel.data.local.dao.StepDao
import com.example.fit_sentinel.data.local.entity.StepDataEntity
import com.example.fit_sentinel.data.remote.ApiService
import com.example.fit_sentinel.data.remote.dto.AnalysisRequest
import com.example.fit_sentinel.data.remote.dto.AnalysisResponse
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.domain.repository.StepSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class StepRepositoryImpl @Inject constructor(
    private val stepDao: StepDao,
    private val apiService: ApiService,
    private val stepSensorManager: StepSensorManager
) : StepRepository {

    override val stepsFromSensor: Flow<Int> = stepSensorManager.currentSteps
    override val isSensorAvailable: Boolean = stepSensorManager.isSensorAvailable
    override val sensorMode: Flow<SensorMode> = stepSensorManager.sensorMode

    // Combine stored steps for today with live sensor steps
    override suspend fun getTodaysTotalSteps(): Int {
        val now = Calendar.getInstance()
        val startOfDay = now.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1 // End of the day

        // Assuming getTotalStepsForDay sums up entries for the day
        return stepDao.getTotalStepsForDay(startOfDay, endOfDay) ?: 0
    }

    override suspend fun saveSteps(steps: Int) {
        withContext(Dispatchers.IO) {
            val stepData = StepDataEntity(
                timestamp = System.currentTimeMillis(),
                steps = steps // Decide saving strategy (total vs delta)
            )
            stepDao.insertStepData(stepData)
        }
    }

    override fun getStepHistory(): Flow<List<StepDataEntity>> {
        return stepDao.getAllStepData()
    }

    override suspend fun getStepAnalysis(request: AnalysisRequest): Result<AnalysisResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.analyzeSteps(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "API Error: ${response.code()}"
                    Log.e("StepRepository", "API Analysis Error: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e("StepRepository", "Network/Analysis Exception", e)
                Result.failure(e)
            }
        }
    }

    override fun startStepTracking() {
        stepSensorManager.startListening()
    }

    override fun stopStepTracking() {
        stepSensorManager.stopListening()
        // Maybe save the final count for the session here?
        CoroutineScope(Dispatchers.IO).launch {
            val finalSessionSteps =
                stepsFromSensor.first() // Get last emitted value (needs careful handling)
            saveSteps(finalSessionSteps) // Decide what 'steps' means here
        }
    }
}