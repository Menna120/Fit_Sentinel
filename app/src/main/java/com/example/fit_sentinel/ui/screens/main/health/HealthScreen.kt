package com.example.fit_sentinel.ui.screens.main.health

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.ui.common.user_data_input_cards.WeightHeightCard

@Composable
fun HealthScreen(
    modifier: Modifier = Modifier,
    viewModel: HealthViewModel = hiltViewModel(),
    onExerciseClick: (index: Int) -> Unit,
) {
    val state = viewModel.state.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    var editedWeight by remember { mutableFloatStateOf(state.value.userProfile.weight) }
    var editedWeightUnit by remember { mutableStateOf(WeightUnit.Kg) }
    var editedHeight by remember { mutableIntStateOf(state.value.userProfile.height) }
    var editedHeightUnit by remember { mutableStateOf(HeightUnit.Cm) }

    LaunchedEffect(state.value.userProfile) {
        editedWeight = state.value.userProfile.weight
        editedHeight = state.value.userProfile.height
        editedWeightUnit = WeightUnit.Kg
        editedHeightUnit = HeightUnit.Cm
    }

    HealthLayout(
        value = state.value.userProfile.bmi,
        categoryLabel = state.value.userProfile.bmiCategory,
        exercises = state.value.recommendations,
        modifier = modifier,
        onExerciseClick = onExerciseClick,
        onEditClick = {
            showEditDialog = true
            editedWeight = state.value.userProfile.weight
            editedHeight = state.value.userProfile.height
            editedWeightUnit = WeightUnit.Kg
            editedHeightUnit = HeightUnit.Cm
        }
    )

    if (showEditDialog) {
        WeightHeightCard(
            initialWeight = editedWeight,
            initialHeight = editedHeight,
            onWeightChange = { weight, unit ->
                viewModel.updateWeight(weight, unit)
            },
            onHeightChange = { height, unit ->
                viewModel.updateHeight(height, unit)
            },
            onDismiss = {
                viewModel.saveUserData(
                    editedWeight,
                    editedWeightUnit,
                    editedHeight,
                    editedHeightUnit
                )
                showEditDialog = false
            }
        )
    }
}