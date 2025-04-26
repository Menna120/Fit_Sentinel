package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _totalSteps = MutableStateFlow(3) // Let's assume 3 steps for this example
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

    // Optional: Reset progress
    fun resetProgress() {
        _currentStep.value = 1
    }
}