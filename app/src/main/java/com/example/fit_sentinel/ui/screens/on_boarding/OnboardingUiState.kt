package com.example.fit_sentinel.ui.screens.on_boarding

import com.example.fit_sentinel.data.model.Gender

data class OnboardingUiState(
    val name: String = "",
    val gender: Gender = Gender.PREFER_NOT_TO_SAY,
    val age: Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f,
    val goalWeight: Float = 0f,
    val chronicDiseases: String = "",
    val isSmoker: Boolean = false,
    val onboardingComplete: Boolean = false,
)
