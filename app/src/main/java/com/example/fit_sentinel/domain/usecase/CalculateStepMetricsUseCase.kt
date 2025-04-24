package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.StepMetrics
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.repository.UserProfileRepository
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateStepMetricsUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository // Inject repository to get user data
) {

    // Average stride length estimations (in meters)
    // These are rough averages; actual stride varies greatly.
    private fun estimateStrideLength(profile: UserProfile?): Double {
        // More accurate: use (heightCm * factor) or (heightCm * factor + age * factor)
        // Even better: allow user to input their stride length
        return when (profile?.gender) {
            Gender.Male -> 0.78 // meters
            Gender.Female -> 0.66 // meters
            else -> 0.70 // Default average
        }
        // A common formula is Height (in cm) * 0.414 for men and 0.413 for women, then convert to meters
        // return (profile?.heightCm ?: 170.0) * (if (profile?.gender == Gender.Male) 0.00414 else 0.00413)
    }

    // Average walking speed (in km/h)
    private val averageWalkingSpeedKmH = 5.0 // kilometers per hour

    // Calories burned per step (estimation)
    // This is a very rough estimate. More accurate methods use METs, weight, and time/distance.
    // A common formula is Calories = METs * Weight (kg) * Duration (hours)
    // For walking, METs is around 3.5-4.0.
    // We can estimate calories per step based on average distance per step and average speed.
    // Or use a simplified formula like Calories per km * Distance (km)
    private fun estimateCaloriesPerKm(profile: UserProfile?): Double {
        // Rough estimation: depends heavily on weight.
        // A common value is around 50-60 kcal per km for an average adult.
        // Let's use a formula: Calories per minute = (METs * weight in kg * 3.5) / 200
        // METs for walking ~ 3.5-4.0. Let's use 3.8
        // Time to walk 1 km at 5 km/h = 1 km / 5 km/h = 0.2 hours = 12 minutes
        // Calories per km = Calories per minute * 12 minutes
        // Calories per km = ((3.8 * (profile?.weightKg ?: 70.0) * 3.5) / 200) * 12
        // Simplified: ~0.75 * weight in kg (very rough)
        return 0.75 * (profile?.weightKg ?: 70.0) // Estimated calories per km
    }


    /**
     * Calculates step metrics.
     * @param steps The total number of steps.
     * @return StepMetrics containing distance, calories, and estimated time.
     */
    suspend operator fun invoke(steps: Int): StepMetrics {
        // Fetch the user profile data
        val userProfile = userProfileRepository.getUserProfile()

        // 1. Estimate Distance
        val strideLengthMeters = estimateStrideLength(userProfile)
        val distanceMeters = steps * strideLengthMeters
        val distanceKm = distanceMeters / 1000.0

        // 2. Estimate Time (based on average walking speed)
        // This is an ESTIMATION. Actual time depends on the user's actual walking speed.
        // If you were tracking the duration of the 'isRecording' state, you could use that.
        val estimatedTimeHours = distanceKm / averageWalkingSpeedKmH
        val estimatedTimeMinutes = (estimatedTimeHours * 60).roundToInt()

        // 3. Estimate Calories
        val caloriesPerKm = estimateCaloriesPerKm(userProfile)
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