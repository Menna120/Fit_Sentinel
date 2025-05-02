package com.example.fit_sentinel.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiRequest(
    val age: Int,
    val height: Int,
    val activity_lvl: String,
    val body_goal: Float,
    val current_weight: Float,
)