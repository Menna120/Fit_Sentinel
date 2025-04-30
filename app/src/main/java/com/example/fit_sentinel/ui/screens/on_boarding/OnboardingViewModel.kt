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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    val isCurrentPageValid: StateFlow<Boolean> =
        uiState.combine(uiState) { _, state ->
            when (OnboardingScreen.entries.getOrNull(state.currentPageIndex)) {
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
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )

    val showSkipButton: StateFlow<Boolean> =
        uiState.combine(uiState) { _, state ->
            OnboardingScreen.entries.getOrNull(state.currentPageIndex) == OnboardingScreen.ILLNESS
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    val showPreviousButton: StateFlow<Boolean> =
        uiState.combine(uiState) { _, state ->
            state.currentPageIndex > OnboardingScreen.INTRO.ordinal
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    val isLastPage: StateFlow<Boolean> =
        uiState.combine(uiState) { _, state ->
            state.currentPageIndex == OnboardingScreen.entries.size - 1
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
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
        _uiState.update { it.copy(selectedWeightValue = value) }
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

    fun onTargetWeightChange(targetWeight: Float) {
        _uiState.update { it.copy(targetWeight = targetWeight) }
    }

    fun onNextPage() {
        if (_uiState.value.currentPageIndex < OnboardingScreen.entries.size - 1) {
            _uiState.update { it.copy(currentPageIndex = it.currentPageIndex + 1) }
        }
    }

    fun onPreviousPage() {
        if (_uiState.value.currentPageIndex > OnboardingScreen.INTRO.ordinal) {
            _uiState.update { it.copy(currentPageIndex = it.currentPageIndex - 1) }
        }
    }

    fun onSkipIllness() {
        val illnessPageIndex = OnboardingScreen.ILLNESS.ordinal
        if (_uiState.value.currentPageIndex == illnessPageIndex) {
            _uiState.update { it.copy(currentPageIndex = illnessPageIndex + 1) }
        }
    }

    fun onOnboardingComplete() {
        saveUser()
        viewModelScope.launch {
            isOnboardingCompletedUseCase.save(true)
        }
    }

    private fun saveUser() {
        viewModelScope.launch {
            val newUser = UserProfile(
                name = _uiState.value.name,
                gender = _uiState.value.gender ?: Gender.Male,
                age = _uiState.value.selectedAge,
                weight = _uiState.value.selectedWeightValue.toFloat(),
                weightUnit = _uiState.value.selectedWeightUnit,
                height = _uiState.value.selectedHeightValue,
                heightUnit = _uiState.value.selectedHeightUnit,
                goalWeight = _uiState.value.targetWeight,
                chronicDiseases = _uiState.value.illnessDescription,
                isSmoker = _uiState.value.smokingOption == SmokingOption.Smoking
            )
            saveUserUseCase(newUser)
        }
    }
}