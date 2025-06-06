package com.example.fit_sentinel.ui.screens.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.common.MonitorRow
import com.example.fit_sentinel.ui.common.MonthNavigationHeader
import com.example.fit_sentinel.ui.screens.main.home.components.StepProgressDisplay
import com.example.fit_sentinel.ui.screens.main.home.components.TargetStepsCard
import com.example.fit_sentinel.ui.screens.main.home.components.WeekDayPicker
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import java.time.LocalDate

@Composable
fun HomeLayout(
    selectedDate: LocalDate,
    steps: Int,
    targetSteps: Int,
    time: String,
    calories: String,
    distance: String,
    isRecording: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        MonthNavigationHeader(
            currentDate = selectedDate,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth
        )

        Text(
            text = "Today",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )

        WeekDayPicker(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )

        TargetStepsCard(targetSteps, Modifier.align(Alignment.CenterHorizontally))

        StepProgressDisplay(
            steps,
            targetSteps,
            isRecording,
            onButtonClick,
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.7f)
        )

        MonitorRow(time, calories, distance)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLayoutPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val onPreviousMonth = { selectedDate = selectedDate.minusMonths(1) }
    val onNextMonth = { selectedDate = selectedDate.plusMonths(1) }

    Fit_SentinelTheme {
        HomeLayout(
            selectedDate,
            0,
            3000,
            "0",
            "0",
            "0",
            false,
            onPreviousMonth,
            onNextMonth,
            {},
            {})
    }
}