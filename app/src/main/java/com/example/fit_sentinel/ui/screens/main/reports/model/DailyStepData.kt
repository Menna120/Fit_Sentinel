package com.example.fit_sentinel.ui.screens.main.reports.model

data class DailyStepData(
    val dayLabel: Double,
    val steps: Double
)

val sampleDailyStepData = listOf(
    DailyStepData(2.01, 3000.0),
    DailyStepData(3.65, 5000.0),
    DailyStepData(4.00, 2000.0),
    DailyStepData(5.34, 5600.0),
    DailyStepData(6.98, 5200.0),
    DailyStepData(7.01, 3500.0),
    DailyStepData(8.01, 3800.0)
)
