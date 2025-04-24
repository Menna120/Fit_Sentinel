package com.example.fit_sentinel.ui.screens.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    HomeLayout(
        selectedDate = uiState.selectedDate,
        steps = uiState.totalSteps,
        targetSteps = uiState.targetSteps,
        time = uiState.time,
        calories = uiState.calories,
        distance = uiState.distance,
        onPreviousMonth = viewModel::onPreviousMonthClicked,
        onNextMonth = viewModel::onNextMonthClicked,
        onDateSelected = viewModel::onDateSelected,
        onButtonClick = viewModel::onStepProgressButtonClicked,
        modifier = modifier
    )
}
