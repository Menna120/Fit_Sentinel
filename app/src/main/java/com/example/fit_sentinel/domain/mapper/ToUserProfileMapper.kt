package com.example.fit_sentinel.domain.mapper

import com.example.fit_sentinel.data.local.entity.UserDataEntity
import com.example.fit_sentinel.domain.model.UserProfile

fun UserDataEntity?.toUserProfile(): UserProfile {
    if (this == null) {
        return UserProfile()
    }
    return UserProfile(
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
        bmi = this.bmi,
        bmiCategory = this.bmiCategory
    )
}