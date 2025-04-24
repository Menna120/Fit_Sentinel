package com.example.fit_sentinel.domain.model

data class UserProfile(
    val weightKg: Double,
    val heightCm: Double,
    val age: Int,
    val gender: Gender
)
