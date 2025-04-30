package com.example.fit_sentinel.ui.screens.main.settings

import com.example.fit_sentinel.domain.model.UserProfile

data class SettingUiState(
    val user: UserProfile = UserProfile(),
    val showDialog: Boolean = false,
    val editingField: EditableField? = null
)
