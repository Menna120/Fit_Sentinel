package com.example.fit_sentinel.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val reps: Int,
    val sets: Int,
    val description: String,
    val exercise_name: String,
)