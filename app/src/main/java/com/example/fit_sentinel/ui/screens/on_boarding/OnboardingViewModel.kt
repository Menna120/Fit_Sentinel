package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.usecase.shared_preferences.IsFirstLaunchUseCase
import com.example.fit_sentinel.domain.usecase.user_data.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val isFirstLaunchUseCase: IsFirstLaunchUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
) : ViewModel() {
    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _totalSteps = MutableStateFlow(9)
    val totalSteps: StateFlow<Int> = _totalSteps.asStateFlow()

    fun nextStep() {
        _currentStep.update { current ->
            if (current < _totalSteps.value) current + 1 else current
        }
    }

    fun previousStep() {
        _currentStep.update { current ->
            if (current > 1) current - 1 else current
        }
    }

    fun saveUser() {
        viewModelScope.launch {
            val newUser = UserProfile(
                name = _uiState.value.name,
                gender = _uiState.value.gender,
                age = _uiState.value.age,
                weight = _uiState.value.weight,
                height = _uiState.value.height,
                activityLevel = "Normal",
                goalWeight = _uiState.value.goalWeight,
                chronicDiseases = _uiState.value.chronicDiseases,
                isSmoker = _uiState.value.isSmoker
            )
            updateUserProfileUseCase(newUser)
        }
    }

    fun completeOnboarding() {
        isFirstLaunchUseCase.save(true)
        _uiState.update { it.copy(onboardingComplete = true) }
        saveUser()

    }
}