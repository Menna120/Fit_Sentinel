package com.example.fit_sentinel.data.repository

import android.util.Log
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.domain.repository.DailyStepsRepository
import com.example.fit_sentinel.domain.repository.StepMetricsRepository
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.domain.repository.StepSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class StepRepositoryImpl @Inject constructor(
    private val dailyStepsRepo: DailyStepsRepository,
    private val stepSensorManager: StepSensorManager,
    private val applicationScope: CoroutineScope,
    private val stepMetricsRepo: StepMetricsRepository
) : StepRepository {

    override val stepsFromSensor: Flow<Int> = stepSensorManager.currentSteps
    override val isSensorAvailable: Boolean = stepSensorManager.isSensorAvailable
    override val sensorMode: Flow<SensorMode> = stepSensorManager.sensorMode

    override fun getCurrentSessionStepsFlow(): Flow<Int> = stepsFromSensor

    override suspend fun getTodaySavedSteps(): Int {
        val dailySteps = dailyStepsRepo.getDailySteps(LocalDate.now())
        return dailySteps?.totalSteps ?: 0
    }

    override suspend fun saveSteps(steps: Int) {
        val today = LocalDate.now()
        val savedDailySteps = dailyStepsRepo.getDailySteps(today) ?: DailyStepsEntity(
            date = today,
            totalSteps = 0,
            lastUpdateTime = System.currentTimeMillis(),
            distanceKm = null,
            caloriesBurned = null,
            estimatedTimeMinutes = null
        )

        val updatedTotalSteps = savedDailySteps.totalSteps + steps
        val metrics = stepMetricsRepo.getStepMetric(
            updatedTotalSteps,
            savedDailySteps.estimatedTimeMinutes
        )
        val updatedDailySteps = savedDailySteps.copy(
            totalSteps = updatedTotalSteps,
            lastUpdateTime = System.currentTimeMillis(),
            distanceKm = metrics.distanceKm,
            caloriesBurned = metrics.caloriesBurned,
            estimatedTimeMinutes = metrics.estimatedTimeMinutes
        )
        dailyStepsRepo.insertOrUpdateDailySteps(updatedDailySteps)

        Log.d(
            "StepRepository",
            "Saved steps for $today: Total: $updatedTotalSteps, Delta: $steps. Metrics: Distance=${metrics.distanceKm}km, Calories=${metrics.caloriesBurned}kcal, Time=${metrics.estimatedTimeMinutes}min"
        )
    }

    override fun getStepHistory(): Flow<List<DailyStepsEntity>> {
        return dailyStepsRepo.getAllDailySteps()
    }

    override fun startStepTracking() = stepSensorManager.startListening()

    override fun stopStepTracking() {
        applicationScope.launch {
            stepsFromSensor.collect {
                saveSteps(it)
                stepSensorManager.stopListening()
            }
        }
    }
}