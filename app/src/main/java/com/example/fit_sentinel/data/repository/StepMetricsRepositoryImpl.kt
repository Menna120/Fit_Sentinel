package com.example.fit_sentinel.data.repository

import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.StepMetrics
import com.example.fit_sentinel.domain.repository.StepMetricsRepository
import com.example.fit_sentinel.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StepMetricsRepositoryImpl @Inject constructor(
    private val userDataRepository: UserDataRepository
) : StepMetricsRepository {
    companion object {
        private const val DEFAULT_WEIGHT_KG = 70f
        private const val DEFAULT_HEIGHT_CM = 170
        private const val FALLBACK_STRIDE_LENGTH_M = 0.7
        private const val FALLBACK_WALKING_SPEED_KMH = 4.0
    }

    private fun estimateStrideLength(heightCm: Int, gender: Gender): Double {
        if (heightCm <= 0) return FALLBACK_STRIDE_LENGTH_M

        val factor = when (gender) {
            Gender.Male -> 0.415
            Gender.Female -> 0.413
        }
        return (heightCm * factor) / 100.0
    }

    private fun getWalkingMet(speedKmH: Double): Double {
        return when {
            speedKmH < 2.0 -> 2.0
            speedKmH < 3.0 -> 2.8
            speedKmH < 4.0 -> 3.0
            speedKmH < 5.0 -> 3.5
            speedKmH < 6.5 -> 4.3
            else -> 5.0
        }
    }

    override suspend fun getStepMetric(steps: Int, durationMillis: Long?): StepMetrics {
        val userData = userDataRepository.getUserData().firstOrNull()

        val weightKg = userData?.weight?.takeIf { it > 0 } ?: DEFAULT_WEIGHT_KG
        val heightCm = userData?.height?.takeIf { it > 0 } ?: DEFAULT_HEIGHT_CM
        val gender = userData?.gender ?: Gender.Male

        if (steps <= 0) return StepMetrics.EMPTY

        val strideLengthMeters = estimateStrideLength(heightCm, gender)
        val distanceKm = (steps * strideLengthMeters) / 1000.0
        val validDistanceKm = distanceKm.coerceAtLeast(0.0)

        val (durationHours, averageSpeedKmH) = calculateDurationAndSpeed(
            validDistanceKm,
            durationMillis
        )

        val caloriesBurned = calculateCaloriesBurned(
            weightKg = weightKg,
            distanceKm = validDistanceKm,
            durationHours = durationHours,
            averageSpeedKmH = averageSpeedKmH
        )

        val estimatedTimeMinutes: Long? = if (durationMillis != null && durationMillis > 0) {
            TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        } else if (validDistanceKm > 0) {
            (validDistanceKm / FALLBACK_WALKING_SPEED_KMH * 60.0).toLong()
        } else {
            0L
        }


        return StepMetrics(
            distanceKm = validDistanceKm,
            caloriesBurned = caloriesBurned.coerceAtLeast(0.0),
            estimatedTimeMinutes = estimatedTimeMinutes?.coerceAtLeast(0L) ?: 0L
        )
    }

    private fun calculateDurationAndSpeed(
        distanceKm: Double,
        durationMillis: Long?
    ): Pair<Double, Double?> {
        return if (durationMillis != null && durationMillis > 0) {
            val hours = durationMillis.toHours()
            val speed = if (hours > 0) distanceKm / hours else 0.0
            Pair(hours, speed)
        } else {
            Pair(0.0, null)
        }
    }

    private fun calculateCaloriesBurned(
        weightKg: Float,
        distanceKm: Double,
        durationHours: Double,
        averageSpeedKmH: Double?
    ): Double {
        return if (averageSpeedKmH != null && averageSpeedKmH > 0) {
            val met = getWalkingMet(averageSpeedKmH)
            met * weightKg * durationHours
        } else {
            val met = getWalkingMet(FALLBACK_WALKING_SPEED_KMH)
            val estimatedHours = distanceKm / FALLBACK_WALKING_SPEED_KMH
            met * weightKg * estimatedHours
        }
    }

    private fun Long.toHours() = this.toDouble() / (1000 * 60 * 60)
}