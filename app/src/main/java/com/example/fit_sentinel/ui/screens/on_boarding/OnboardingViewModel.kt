package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.lifecycle.ViewModel
import com.example.fit_sentinel.domain.usecase.shared_preferences.IsFirstLaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val isFirstLaunchUseCase: IsFirstLaunchUseCase
) : ViewModel() {
    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

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

    fun completeOnboarding() = isFirstLaunchUseCase.save(true)
}