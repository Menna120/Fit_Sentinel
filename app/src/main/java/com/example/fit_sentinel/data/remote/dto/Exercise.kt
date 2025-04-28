package com.example.fit_sentinel.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val description: String,
    val exercise_name: String,
    val reps: Int,
    val sets: Int
)