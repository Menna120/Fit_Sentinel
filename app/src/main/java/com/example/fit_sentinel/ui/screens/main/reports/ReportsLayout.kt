package com.example.fit_sentinel.ui.screens.main.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.common.MonitorRow
import com.example.fit_sentinel.ui.common.MonthNavigationHeader
import com.example.fit_sentinel.ui.screens.main.reports.components.StaticsChart
import com.example.fit_sentinel.ui.screens.main.reports.components.TotalStepsCard
import com.example.fit_sentinel.ui.screens.main.reports.components.progress_calendar.ProgressCalendar
import com.example.fit_sentinel.ui.screens.main.reports.model.DailyStepData
import com.example.fit_sentinel.ui.screens.main.reports.model.DayProgress
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import java.time.LocalDate

@Composable
fun ReportsLayout(
    currentDate: LocalDate,
    totalSteps: Double,
    time: String,
    calories: String,
    distance: String,
    dailyProgress: List<DayProgress>,
    dailyStepData: List<DailyStepData>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        MonthNavigationHeader(currentDate, onPreviousMonth, onNextMonth)

        TotalStepsCard(totalSteps)

        MonitorRow(time, calories, distance)

        ProgressCalendar(dailyProgress)

        Text("Statics", style = MaterialTheme.typography.titleLarge)

        StaticsChart(dailyStepData)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportsLayoutPreview() {
    val sampleDailyProgress = remember {
        mutableStateListOf(
            DayProgress(1, 0.8f),
            DayProgress(2, 1.0f),
            DayProgress(3, 0.3f),
            DayProgress(4, 0.9f),
            DayProgress(5, 0.6f),
            DayProgress(6, 0.1f),
            DayProgress(7, 0.75f)
        )
    }
    val sampleDailyStepData = listOf(
        DailyStepData(2.01, 3000.0),
        DailyStepData(3.65, 5000.0),
        DailyStepData(4.00, 2000.0),
        DailyStepData(5.34, 5600.0),
        DailyStepData(6.98, 5200.0),
        DailyStepData(7.01, 3500.0),
        DailyStepData(8.01, 3800.0)
    )

    Fit_SentinelTheme {
        ReportsLayout(
            LocalDate.now(),
            247.345,
            "1h 14min",
            "360",
            "5.64",
            sampleDailyProgress,
            sampleDailyStepData,
            {},
            {}
        )
    }
}