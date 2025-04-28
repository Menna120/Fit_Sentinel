package com.example.fit_sentinel.ui.screens.main.health

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fit_sentinel.ui.screens.main.health.components.HealthLayout

@Composable
fun HealthScreen(
    modifier: Modifier = Modifier,
    viewModel: HealthViewModel = hiltViewModel(),
    onExerciseClick: (index: Int) -> Unit,
) {
    val state = viewModel.state.collectAsState()

    HealthLayout(
        value = state.value.bmi,
        categoryLabel = state.value.categoryLabel,
        exercises = state.value.recommendations,
        modifier = modifier,
        onExerciseClick = onExerciseClick
    )
}