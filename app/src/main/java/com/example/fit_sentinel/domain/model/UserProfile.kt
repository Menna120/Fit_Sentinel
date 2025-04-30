package com.example.fit_sentinel.domain.model

data class UserProfile(
    val name: String = "",
    val gender: Gender = Gender.Male,
    val age: Int = 0,
    val weight: Float = 0f,
    val weightUnit: WeightUnit = WeightUnit.Kg,
    val height: Int = 0,
    val heightUnit: HeightUnit = HeightUnit.Cm,
    val activityLevel: String = "",
    val goalWeight: Float = 0f,
    val chronicDiseases: String = "",
    val isSmoker: Boolean = false,
    val bmi: Float = 0f,
    val bmiCategory: String = ""
)
