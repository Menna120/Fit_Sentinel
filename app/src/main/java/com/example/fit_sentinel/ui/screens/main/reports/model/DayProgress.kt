package com.example.fit_sentinel.ui.screens.main.reports.model

data class DayProgress(
    val dayOfMonth: Int,
    val progress: Float
)

val sampleDailyProgress = listOf(
    DayProgress(1, 0.8f),
    DayProgress(2, 1.0f),
    DayProgress(3, 0.3f),
    DayProgress(4, 0.9f),
    DayProgress(5, 0.6f),
    DayProgress(6, 0.1f),
    DayProgress(7, 0.73f)
)
