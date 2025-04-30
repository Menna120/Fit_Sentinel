package com.example.fit_sentinel.ui.screens.main.health

import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.domain.model.UserProfile

data class HealthUiState(
    val recommendations: List<Exercise> = emptyList(),
    val userProfile: UserProfile = UserProfile(),
)
