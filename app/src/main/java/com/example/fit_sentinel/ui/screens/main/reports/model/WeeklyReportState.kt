package com.example.fit_sentinel.ui.screens.main.reports.model

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.utils.Constants.Companion.TARGET_STEPS
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class WeeklyReportState(
    val startDate: LocalDate = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)),
    val endDate: LocalDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)),
    val weeklyStepsData: List<DailyStepsEntity> = emptyList(),
    val dailyProgressList: List<DayProgress> = emptyList(),
    val dailyStepDataList: List<DailyStepData> = emptyList(),
    val totalStepsThisWeek: Double = 0.0,
    val totalTimeThisWeek: String = "0",
    val totalCaloriesThisWeek: String = "0",
    val totalDistanceThisWeek: String = "0",
    val targetSteps: Int = TARGET_STEPS
)
