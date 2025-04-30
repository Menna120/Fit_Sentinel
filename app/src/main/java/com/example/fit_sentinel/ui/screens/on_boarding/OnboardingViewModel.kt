package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.domain.usecase.shared_preferences.IsOnboardingCompletedUseCase
import com.example.fit_sentinel.domain.usecase.user_data.SaveUserProfileUseCase
import com.example.fit_sentinel.ui.screens.on_boarding.model.OnboardingScreen
import com.example.fit_sentinel.ui.screens.on_boarding.model.OnboardingUiState
import com.example.fit_sentinel.ui.screens.on_boarding.model.SmokingOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserProfileUseCase,
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun isCurrentPageValid(currentPageIndex: Int): StateFlow<Boolean> =
        uiState.map { state ->
            when (OnboardingScreen.entries.getOrNull(currentPageIndex)) {
                OnboardingScreen.INTRO -> true
                OnboardingScreen.NAME -> state.name.isNotBlank()
                OnboardingScreen.GENDER -> state.gender != null
                OnboardingScreen.SMOKING -> state.smokingOption != null
                OnboardingScreen.ILLNESS -> true
                OnboardingScreen.WEIGHT -> state.selectedWeightValue > 0
                OnboardingScreen.HEIGHT -> state.selectedHeightValue > 0
                OnboardingScreen.AGE -> state.selectedAge > 0
                OnboardingScreen.TARGET_WEIGHT -> true
                null -> false
            }
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )

    fun showPreviousButton(currentPageIndex: Int): StateFlow<Boolean> =
        uiState.map {
            currentPageIndex > OnboardingScreen.INTRO.ordinal
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    fun isLastPage(currentPageIndex: Int): StateFlow<Boolean> =
        uiState.map {
            currentPageIndex == OnboardingScreen.entries.size - 1
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onGenderSelected(gender: Gender) {
        _uiState.update { it.copy(gender = gender) }
    }

    fun onSmokingOptionClick(smokingOption: SmokingOption) {
        _uiState.update { it.copy(smokingOption = smokingOption) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(illnessDescription = description) }
    }

    fun onWeightUnitSelected(unit: WeightUnit) {
        _uiState.update { it.copy(selectedWeightUnit = unit) }
    }

    fun onWeightValueChange(value: Int) {
        _uiState.update {
            it.copy(
                selectedWeightValue = value,
                targetWeightPlaceholder = value.toString()
            )
        }
    }

    fun onHeightUnitSelected(unit: HeightUnit) {
        _uiState.update { it.copy(selectedHeightUnit = unit) }
    }

    fun onHeightValueChange(value: Int) {
        _uiState.update { it.copy(selectedHeightValue = value) }
    }

    fun onAgeChange(age: Int) {
        _uiState.update { it.copy(selectedAge = age) }
    }

    fun onTargetWeightChange(targetWeightString: String) {
        _uiState.update {
            val weightValue = targetWeightString.toFloatOrNull() ?: 0f
            it.copy(targetWeight = weightValue, targetWeightPlaceholder = targetWeightString)
        }
    }

    fun onOnboardingComplete() {
        viewModelScope.launch {
            saveUser()
            isOnboardingCompletedUseCase.save(true)
        }
    }

    private suspend fun saveUser() {
        val currentState = _uiState.value
        val newUser = UserProfile(
            name = currentState.name.trim(),
            gender = currentState.gender ?: Gender.Male,
            age = currentState.selectedAge.coerceAtLeast(1),
            weight = currentState.selectedWeightValue.toFloat().coerceAtLeast(0.1f),
            weightUnit = currentState.selectedWeightUnit,
            height = currentState.selectedHeightValue.coerceAtLeast(1),
            heightUnit = currentState.selectedHeightUnit,
            goalWeight = currentState.targetWeight.coerceAtLeast(0.1f),
            chronicDiseases = currentState.illnessDescription.trim(),
            isSmoker = currentState.smokingOption == SmokingOption.Smoking
        )
        saveUserUseCase(newUser)
    }
}