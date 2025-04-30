package com.example.fit_sentinel.domain.mapper

import com.example.fit_sentinel.data.local.entity.UserDataEntity
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.model.WeightUnit
import kotlin.math.pow

fun UserProfile.toUserDataEntity(): UserDataEntity {
    val weightKg = when (this.weightUnit) {
        WeightUnit.Kg -> this.weight
        WeightUnit.Lbs -> this.weight * 0.45359237f
    }

    val heightCm = when (this.heightUnit) {
        HeightUnit.Cm -> this.height.toFloat()
        HeightUnit.Ft -> this.height * 30.48f
    }

    val bmi = calculateBmi(weightKg, heightCm)
    val bmiCategory = calculateBmiCategory(bmi)

    return UserDataEntity(
        id = 1,
        name = this.name,
        gender = this.gender,
        age = this.age,
        weight = this.weight,
        weightUnit = this.weightUnit,
        height = this.height,
        heightUnit = this.heightUnit,
        activityLevel = this.activityLevel,
        goalWeight = this.goalWeight,
        chronicDiseases = this.chronicDiseases,
        isSmoker = this.isSmoker,
        bmi = bmi,
        bmiCategory = bmiCategory
    )
}

private fun calculateBmi(weightKg: Float, heightCm: Float): Float {
    if (weightKg <= 0f || heightCm <= 0f) return 0f
    val heightM = heightCm / 100f
    return weightKg / (heightM.pow(2))
}

private fun calculateBmiCategory(bmi: Float): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi >= 18.5 && bmi <= 24.9 -> "Normal weight"
        bmi >= 25.0 && bmi <= 29.9 -> "Overweight"
        bmi >= 30.0 && bmi <= 34.9 -> "Obesity Class I"
        bmi >= 35.0 && bmi <= 39.9 -> "Obesity Class II"
        bmi >= 40.0 -> "Obesity Class III"
        else -> "Unknown"
    }
}