package com.example.fit_sentinel.domain.model

data class StepMetrics(
    val distanceKm: Double,
    val caloriesBurned: Double,
    val estimatedTimeMinutes: Long
) {
    companion object {
        val EMPTY = StepMetrics(0.0, 0.0, 0L)
    }
}
