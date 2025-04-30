package com.example.fit_sentinel.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiRequest(
    val age: Int,
    val height: Int,
    @SerialName("activity_lvl") val activityLevel: String,
    @SerialName("body_goal") val bodyGoal: Float,
    @SerialName("current_weight") val weight: Float,
)