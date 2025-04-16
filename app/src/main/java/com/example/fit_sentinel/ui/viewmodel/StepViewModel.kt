package com.example.fit_sentinel.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.remote.dto.AnalysisRequest
import com.example.fit_sentinel.data.remote.dto.AnalysisResponse
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.domain.usecase.GetStepAnalysisUseCase
import com.example.fit_sentinel.domain.usecase.StartStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.StopStepTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StepCounterUiState(
    val currentSessionSteps: Int = 0,
    val totalStepsToday: Int = 0,
    val lastAnalysis: AnalysisResponse? = null,
    val isLoadingAnalysis: Boolean = false,
    val errorMessage: String? = null,
    val isTracking: Boolean = false,
    val isSensorAvailable: Boolean = true,
    val sensorMode: SensorMode = SensorMode.UNAVAILABLE
)

@HiltViewModel
class StepViewModel @Inject constructor(
    private val repository: StepRepository,
    private val getStepAnalysisUseCase: GetStepAnalysisUseCase,
    private val startStepTrackingUseCase: StartStepTrackingUseCase,
    private val stopStepTrackingUseCase: StopStepTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StepCounterUiState())
    val uiState: StateFlow<StepCounterUiState> = _uiState.asStateFlow()

    // State for steps stored in DB today (separate from live session steps)
    private val _storedStepsToday = MutableStateFlow(0)

    init {
        // Initial check for overall sensor availability
        _uiState.update { it.copy(isSensorAvailable = repository.isSensorAvailable) }
        // Load steps previously stored for today
        loadInitialStoredSteps()
        // Start observing combined sensor data (steps, mode, stored)
        observeSensorData()
    }

    private fun loadInitialStoredSteps() {
        viewModelScope.launch {
            _storedStepsToday.value = repository.getTodaysTotalSteps()
            // Update total in UI state based on this initial value (session steps are 0 initially)
            _uiState.update { currentState ->
                currentState.copy(totalStepsToday = _storedStepsToday.value)
            }
            Log.d("ViewModel", "Initial stored steps loaded: ${_storedStepsToday.value}")
        }
    }

    // Combined observer for sensor steps, sensor mode, and stored steps
    private fun observeSensorData() {
        viewModelScope.launch {
            combine(
                repository.stepsFromSensor, // Flow<Int> - session steps from sensor manager
                repository.sensorMode,      // Flow<SensorMode> - current mode from sensor manager
                _storedStepsToday           // StateFlow<Int> - steps stored in DB today
            ) { sessionSteps, mode, storedSteps ->
                // Calculate the combined total steps for the day
                val totalToday = storedSteps + sessionSteps

                Log.d(
                    "ViewModel",
                    "Sensor Data Update -> Session: $sessionSteps, Mode: $mode, Stored: $storedSteps, Total: $totalToday"
                )

                // Create a new state by updating based on all combined flows
                _uiState.update { currentState ->
                    currentState.copy(
                        currentSessionSteps = sessionSteps,
                        totalStepsToday = totalToday,
                        sensorMode = mode, // <-- Update sensor mode in UI state
                        // Update isSensorAvailable based on mode for consistency
                        isSensorAvailable = mode != SensorMode.UNAVAILABLE
                        // Note: isTracking is updated in start/stopTracking methods
                    )
                }
            }.catch { e ->
                // Handle potential errors in the flows
                Log.e("ViewModel", "Error observing sensor data", e)
                _uiState.update { it.copy(errorMessage = "Error reading sensor data.") }
            }.collect() // Start collecting the combined flow
        }
    }


    fun startTracking() {
        // Check availability before attempting to start
        if (_uiState.value.sensorMode == SensorMode.UNAVAILABLE) {
            _uiState.update { it.copy(errorMessage = "Step sensor not available on this device.") }
            Log.w("ViewModel", "Start tracking attempt failed: Sensor unavailable.")
            return
        }
        startStepTrackingUseCase()
        _uiState.update { it.copy(isTracking = true, errorMessage = null) } // Set tracking state
        Log.d("ViewModel", "Tracking Started (Mode: ${_uiState.value.sensorMode})")
    }

    fun stopTracking() {
        // No need to check availability here, just stop if tracking
        if (_uiState.value.isTracking) {
            stopStepTrackingUseCase()
            _uiState.update { it.copy(isTracking = false) } // Unset tracking state
            Log.d("ViewModel", "Tracking Stopped")
            // Consider saving the total steps when tracking stops
            // saveCurrentTotalSteps()
        }
    }

    fun requestAnalysis() {
        if (_uiState.value.isLoadingAnalysis) return

        _uiState.update { it.copy(isLoadingAnalysis = true, errorMessage = null) }
        viewModelScope.launch {
            val request = AnalysisRequest(
                userId = "user123", // Replace with actual ID
                stepsToday = _uiState.value.totalStepsToday,
                timestamp = System.currentTimeMillis(),
                // Provide weight if your AI model uses it
                // user_weight_kg = 75.0f
            )
            val result = getStepAnalysisUseCase(request)
            result.onSuccess { analysis ->
                _uiState.update {
                    it.copy(lastAnalysis = analysis, isLoadingAnalysis = false)
                }
            }.onFailure { error ->
                Log.e("ViewModel", "Analysis failed", error)
                _uiState.update {
                    it.copy(
                        errorMessage = "Analysis failed: ${error.message}",
                        isLoadingAnalysis = false
                    )
                }
            }
        }
    }

    fun saveCurrentTotalSteps() {
        val totalSteps = _uiState.value.totalStepsToday
        if (totalSteps > 0) { // Only save if there are steps
            viewModelScope.launch {
                // Decide on save strategy (e.g., replace daily entry or add new entry)
                repository.saveSteps(totalSteps) // TODO: Refine saveSteps logic if needed
                // Update the baseline stored steps after saving
                _storedStepsToday.value = totalSteps
                Log.d("ViewModel", "Attempted to save total steps: $totalSteps")
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // onCleared remains the same
    override fun onCleared() {
        // Optional: Ensure tracking is stopped if ViewModel is destroyed
        // stopTracking() // Consider implications if service manages tracking
        super.onCleared()
    }
}