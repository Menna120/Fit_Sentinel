package com.example.fit_sentinel.ui.screens.main.home

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.utils.Constants.Companion.TARGET_STEPS
import java.time.LocalDate

data class HomeUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val totalSteps: Int = 0,
    val targetSteps: Int = TARGET_STEPS,
    val historicalData: List<DailyStepsEntity> = emptyList(),
    val isRecording: Boolean = false,
    val errorMessage: String? = null,
    val time: String = "0h 0m",
    val calories: String = "0",
    val distance: String = "0.0"
)
