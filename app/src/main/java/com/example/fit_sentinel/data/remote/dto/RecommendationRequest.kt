package com.example.fit_sentinel.data.remote.dto

data class RecommendationRequest(
    val current_weight: String,
    val height: String,
    val age: String,
    val activity_level: String,
    val goal: String
)