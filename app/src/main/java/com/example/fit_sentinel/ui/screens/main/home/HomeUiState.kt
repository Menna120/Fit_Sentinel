package com.example.fit_sentinel.ui.screens.main.home

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import java.time.LocalDate

data class HomeUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val totalSteps: Int = 0,
    val targetSteps: Int = 3000,
    val historicalData: List<DailyStepsEntity> = emptyList(),
    val isRecording: Boolean = false,
    val errorMessage: String? = null,
    val time: String = "0h 0m",
    val calories: String = "0 kcal",
    val distance: String = "0.0 km"
)
