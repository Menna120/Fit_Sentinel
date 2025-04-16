package com.example.fit_sentinel.data.remote.dto

data class AnalysisRequest(
    val userId: String,
    val stepsToday: Int,
    val timestamp: Long,
    val weightKg: Float? = null
)
