package com.example.fit_sentinel.data.remote.dto

data class RecommendationResponse(
    val exercises: List<Exercise>,
    val times_per_week: Int
)