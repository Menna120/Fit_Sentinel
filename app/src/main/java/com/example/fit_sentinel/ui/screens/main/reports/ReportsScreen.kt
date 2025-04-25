package com.example.fit_sentinel.ui.screens.main.reports

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadWeeklyData(state.startDate, state.endDate)
    }

    ReportsLayout(
        currentDate = state.startDate,
        totalSteps = state.totalStepsThisWeek,
        time = state.totalTimeThisWeek,
        calories = state.totalCaloriesThisWeek,
        distance = state.totalDistanceThisWeek,
        dailyProgress = state.dailyProgressList,
        dailyStepData = state.dailyStepDataList,
        onPreviousMonth = { viewModel.goToPreviousWeek() },
        onNextMonth = { viewModel.goToNextWeek() },
        modifier = Modifier.fillMaxSize()
    )
}
