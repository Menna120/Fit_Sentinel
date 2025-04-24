package com.example.fit_sentinel.data.repository

import android.util.Log
import com.example.fit_sentinel.data.local.dao.DailyStepsDao
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.data.remote.ApiService
import com.example.fit_sentinel.data.remote.dto.AnalysisRequest
import com.example.fit_sentinel.data.remote.dto.AnalysisResponse
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.domain.repository.StepSensorManager
import com.example.fit_sentinel.domain.usecase.CalculateStepMetricsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class StepRepositoryImpl @Inject constructor(
    private val dailyStepsDao: DailyStepsDao,
    private val apiService: ApiService,
    private val stepSensorManager: StepSensorManager,
    private val applicationScope: CoroutineScope,
    private val calculateStepMetricsUseCase: CalculateStepMetricsUseCase
) : StepRepository {

    override val stepsFromSensor: Flow<Int> = stepSensorManager.currentSteps
    override val isSensorAvailable: Boolean = stepSensorManager.isSensorAvailable
    override val sensorMode: Flow<SensorMode> = stepSensorManager.sensorMode

    private var lastSavedSensorSteps = 0

    override fun getCurrentSessionStepsFlow(): Flow<Int> = flow {
        stepSensorManager.currentSteps.collect { currentSessionSteps ->
            emit(currentSessionSteps - lastSavedSensorSteps)
        }
    }

    override suspend fun getTodaySavedSteps(): Int {
        val dailySteps = dailyStepsDao.getDailySteps(LocalDate.now())
        return dailySteps?.totalSteps ?: 0
    }

    override suspend fun saveSteps(steps: Int) {
        withContext(Dispatchers.IO) {
            val today = LocalDate.now()
            val savedDailySteps = dailyStepsDao.getDailySteps(today) ?: DailyStepsEntity(
                date = today,
                totalSteps = 0,
                lastUpdateTime = System.currentTimeMillis(),
                distanceKm = null,
                caloriesBurned = null,
                estimatedTimeMinutes = null
            )

            val sessionStepsSinceLastSave = steps - lastSavedSensorSteps

            val updatedTotalSteps = savedDailySteps.totalSteps + sessionStepsSinceLastSave

            val metrics = calculateStepMetricsUseCase(updatedTotalSteps)

            val updatedDailySteps = savedDailySteps.copy(
                totalSteps = updatedTotalSteps,
                lastUpdateTime = System.currentTimeMillis(),
                distanceKm = metrics.distanceKm,
                caloriesBurned = metrics.caloriesBurned,
                estimatedTimeMinutes = metrics.estimatedTimeMinutes
            )

            dailyStepsDao.insertOrUpdateDailySteps(updatedDailySteps)

            lastSavedSensorSteps = steps

            Log.d(
                "StepRepository",
                "Saved steps for $today: Total: $updatedTotalSteps, Delta: $sessionStepsSinceLastSave. Metrics: Distance=${metrics.distanceKm}km, Calories=${metrics.caloriesBurned}kcal, Time=${metrics.estimatedTimeMinutes}min"
            )
        }
    }

    override fun getStepHistory(): Flow<List<DailyStepsEntity>> {
        return dailyStepsDao.getAllDailySteps()
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
        lastSavedSensorSteps = 0
        stepSensorManager.startListening()
    }

    override fun stopStepTracking() {
        stepSensorManager.stopListening()
        applicationScope.launch {
            val finalSessionSteps = stepSensorManager.currentSteps.value
            saveSteps(finalSessionSteps)
        }
    }
}