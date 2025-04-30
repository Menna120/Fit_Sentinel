package com.example.fit_sentinel.ui.screens.on_boarding.model

import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit

data class OnboardingUiState(
    val name: String = "",
    val gender: Gender? = null,
    val smokingOption: SmokingOption? = null,
    val illnessDescription: String = "",
    val selectedWeightUnit: WeightUnit = WeightUnit.Kg,
    val selectedWeightValue: Int = 50,
    val selectedHeightUnit: HeightUnit = HeightUnit.Cm,
    val selectedHeightValue: Int = 170,
    val selectedAge: Int = 20,
    val targetWeight: Float = selectedWeightValue.toFloat(),
    val targetWeightPlaceholder: String = "50",
    val totalPages: Int = OnboardingScreen.entries.size,
    val weightUnits: List<WeightUnit> = WeightUnit.entries,
    val heightUnits: List<HeightUnit> = HeightUnit.entries
)
