package com.example.fit_sentinel.ui.screens.main.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isRecording) {
        Toast.makeText(
            context,
            if (uiState.isRecording) "Session Started" else "Session Ended",
            Toast.LENGTH_SHORT
        ).show()
    }

    HomeLayout(
        selectedDate = uiState.selectedDate,
        steps = uiState.totalSteps,
        targetSteps = uiState.targetSteps,
        time = uiState.time,
        calories = uiState.calories,
        distance = uiState.distance,
        onPreviousMonth = viewModel::onPreviousWeekClicked,
        onNextMonth = viewModel::onNextWeekClicked,
        onDateSelected = viewModel::onDateSelected,
        onButtonClick = viewModel::onStepProgressButtonClicked,
        modifier = modifier
    )
}
