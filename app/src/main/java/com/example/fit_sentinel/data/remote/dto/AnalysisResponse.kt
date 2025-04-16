package com.example.fit_sentinel.data.remote.dto

data class AnalysisResponse(
    val estimatedDistanceKm: Double,
    val estimatedCaloriesBurned: Int,
    val suggestedStepsToday: Int,
    val message: String? = null
)
