package com.example.fit_sentinel.domain.usecase.user_data

import com.example.fit_sentinel.data.local.entity.UserDataEntity
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.domain.repository.UserDataRepository
import javax.inject.Inject
import kotlin.math.pow

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(userProfile: UserProfile) {
        val weightKg = when (userProfile.weightUnit) {
            WeightUnit.KG -> userProfile.weight
            WeightUnit.LBS -> userProfile.weight * 0.45359237f
        }

        val heightCm = when (userProfile.heightUnit) {
            HeightUnit.CM -> userProfile.height
            HeightUnit.FT -> userProfile.height * 30.48f
        }

        val bmi = calculateBmi(weightKg, heightCm)

        val userToUpdate = UserDataEntity(
            name = userProfile.name,
            gender = userProfile.gender,
            age = userProfile.age,
            weight = userProfile.weight,
            height = userProfile.height,
            activityLevel = userProfile.activityLevel,
            goalWeight = userProfile.goalWeight,
            chronicDiseases = userProfile.chronicDiseases,
            isSmoker = userProfile.isSmoker,
            bmi = bmi
        )

        repository.updateUserData(userToUpdate)
    }

    private fun calculateBmi(weightKg: Float, heightCm: Float): Float {
        if (weightKg <= 0f || heightCm <= 0f) return 0f
        val heightM = heightCm / 100f
        return weightKg / (heightM.pow(2))
    }
}