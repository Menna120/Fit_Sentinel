package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.data.local.entity.UserDataEntity
import com.example.fit_sentinel.data.model.Gender
import com.example.fit_sentinel.domain.model.StepMetrics
import com.example.fit_sentinel.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateStepMetricsUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {

    /**
     * Estimates stride length based on user's height and gender.
     * Uses a common formula: Height (cm) * factor (0.414 for men, 0.413 for women).
     * Returns stride length in meters.
     */
    private fun estimateStrideLength(heightCm: Float, gender: Gender): Double {
        // Return a reasonable default if height is invalid (e.g., 0 or negative)
        if (heightCm <= 0) return 0.70 // Default average stride length in meters

        val factor = if (gender == Gender.MALE) 0.414 else 0.413
        val strideCm = heightCm * factor
        return strideCm / 100.0 // Convert cm to meters
    }

    // Average walking speed (in km/h) - Used for estimated time
    private val averageWalkingSpeedKmH = 5.0 // kilometers per hour

    /**
     * Estimates calories burned per kilometer based on user's weight.
     * Uses a simplified heuristic: ~0.75 kcal per kg per km.
     */
    private fun estimateCaloriesPerKm(weightKg: Float): Double {
        // Return a reasonable default if weight is invalid (e.g., 0 or negative)
        if (weightKg <= 0) return 0.75 * 70.0 // Default for an average adult (70kg)

        // Very rough estimation: Calories per km is approximately 0.75 * weight in kg
        return 0.75 * weightKg
    }


    /**
     * Calculates step metrics (distance, estimated calories, estimated time).
     * @param steps The total number of steps.
     * @return StepMetrics containing distance, calories, and estimated time.
     */
    suspend operator fun invoke(steps: Int): StepMetrics {

        // Retrieve user data using firstOrNull() to get the latest value from the Flow
        // and not suspend indefinitely. Returns null if the Flow is empty or produces null.
        val userData: UserDataEntity? = userDataRepository.getUserData().firstOrNull()

        // Extract user profile data, providing default values if userData is null or fields are invalid
        val weightKg =
            userData?.weight?.takeIf { it > 0 } ?: 70f // Default to 70kg if data missing or invalid
        val heightCm = userData?.height?.takeIf { it > 0 }
            ?: 170f // Default to 170cm if data missing or invalid
        val gender = userData?.gender ?: Gender.MALE // Default to MALE if data missing

        // 1. Estimate Distance
        val strideLengthMeters = estimateStrideLength(heightCm, gender)
        val distanceMeters = steps * strideLengthMeters
        val distanceKm = distanceMeters / 1000.0

        // 2. Estimate Time (based on average walking speed)
        // This is an ESTIMATION. Actual time depends on the user's actual walking speed.
        // If you were tracking the duration of the activity, use that instead for accuracy.
        val estimatedTimeHours = distanceKm / averageWalkingSpeedKmH
        // Ensure estimated time is not negative or NaN before rounding
        val estimatedTimeMinutes =
            (estimatedTimeHours * 60).takeIf { !it.isNaN() && it >= 0 }?.roundToInt() ?: 0


        // 3. Estimate Calories
        val caloriesPerKm = estimateCaloriesPerKm(weightKg)
        val caloriesBurned = distanceKm * caloriesPerKm

        return StepMetrics(
            distanceKm = distanceKm,
            caloriesBurned = caloriesBurned,
            estimatedTimeMinutes = estimatedTimeMinutes
        )
    }

    // You might add another invoke function that takes steps and duration if you track session time
    // suspend fun invoke(steps: Int, durationMillis: Long): StepMetrics { ... }
}