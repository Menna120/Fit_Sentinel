package com.example.fit_sentinel.ui.screens.main.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.fit_sentinel.ui.screens.main.reports.model.sampleDailyProgress
import com.example.fit_sentinel.ui.screens.main.reports.model.sampleDailyStepData
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