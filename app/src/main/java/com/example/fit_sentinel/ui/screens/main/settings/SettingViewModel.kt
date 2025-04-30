package com.example.fit_sentinel.ui.screens.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.domain.usecase.user_data.GetUserDataUseCase
import com.example.fit_sentinel.domain.usecase.user_data.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    init {
        updateUiState()
    }

    private fun updateUiState() {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                _uiState.update {
                    it.copy(
                        user
                    )
                }
            }
        }
    }

    private fun updateUserProfile() {
        viewModelScope.launch {
            updateUserProfileUseCase(_uiState.value.user)
        }
    }

    fun updateName(newName: String) {
        _uiState.update { it.copy(user = it.user.copy(name = newName)) }
        updateUserProfile()
        dismissDialog()
    }

    fun updateAge(newAge: String) {
        _uiState.update { it.copy(user = it.user.copy(age = newAge.toInt())) }
        updateUserProfile()
        dismissDialog()
    }

    fun updateGender(newGender: Gender?) {
        _uiState.update { it.copy(user = it.user.copy(gender = newGender as Gender)) }
        updateUserProfile()
        dismissDialog()
    }

    fun updateWeight(newWeight: String, newUnit: WeightUnit?) {
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    weight = newWeight.toFloat(),
                    weightUnit = newUnit as WeightUnit
                )
            )
        }
        updateUserProfile()
        dismissDialog()
    }

    fun updateHeight(newHeight: String, newUnit: HeightUnit?) {
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    height = newHeight.toInt(),
                    heightUnit = newUnit as HeightUnit
                )
            )
        }
        updateUserProfile()
        dismissDialog()
    }

    fun updateTargetWeight(newTargetWeight: String) {
        _uiState.update { it.copy(user = it.user.copy(goalWeight = newTargetWeight.toFloat())) }
        updateUserProfile()
        dismissDialog()
    }

    fun showEditDialog(field: EditableField) {
        _uiState.update { it.copy(showDialog = true, editingField = field) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showDialog = false, editingField = null) }
    }
}