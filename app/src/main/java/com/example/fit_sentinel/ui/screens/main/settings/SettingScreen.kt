package com.example.fit_sentinel.ui.screens.main.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingLayout(
        name = uiState.user.name,
        onNameChange = viewModel::updateName,
        age = uiState.user.age.toString(),
        onAgeChange = viewModel::updateAge,
        gender = uiState.user.gender,
        onGenderChange = viewModel::updateGender,
        weight = uiState.user.weight.toString(),
        weightUnit = uiState.user.weightUnit,
        onWeightChange = viewModel::updateWeight,
        height = uiState.user.height.toString(),
        heightUnit = uiState.user.heightUnit,
        onHeightChange = viewModel::updateHeight,
        targetWeight = uiState.user.goalWeight.toString(),
        onTargetWeightChange = viewModel::updateTargetWeight,
        showDialog = uiState.showDialog,
        editingField = uiState.editingField,
        onDismissDialog = viewModel::dismissDialog,
        onShowDialog = viewModel::showEditDialog
    )
}
