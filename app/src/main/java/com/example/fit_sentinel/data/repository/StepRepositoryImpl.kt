package com.example.fit_sentinel.data.repository

import android.util.Log
import com.example.fit_sentinel.data.local.dao.DailyStepsDao
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.domain.repository.StepMetricsRepository
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.domain.repository.StepSensorManager
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
    private val stepSensorManager: StepSensorManager,
    private val applicationScope: CoroutineScope,
    private val stepMetricsRepository: StepMetricsRepository
) : StepRepository {

    override val stepsFromSensor: Flow<Int> = stepSensorManager.currentSteps
    override val isSensorAvailable: Boolean = stepSensorManager.isSensorAvailable
    override val sensorMode: Flow<SensorMode> = stepSensorManager.sensorMode

    private var lastSavedSensorSteps = 0

    override fun getCurrentSessionStepsFlow(): Flow<Int> = flow {
        stepsFromSensor.collect { currentSessionSteps ->
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

            val metrics = stepMetricsRepository.getStepMetric(updatedTotalSteps)

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

    override fun startStepTracking() {
        lastSavedSensorSteps = 0
        stepSensorManager.startListening()
    }

    override fun stopStepTracking() {
        stepSensorManager.stopListening()
        applicationScope.launch {
            stepsFromSensor.collect {
                saveSteps(it)
            }
        }
    }
}