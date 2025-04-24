package com.example.fit_sentinel.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.domain.usecase.CalculateStepMetricsUseCase
import com.example.fit_sentinel.domain.usecase.GetCurrentSessionStepsUseCase
import com.example.fit_sentinel.domain.usecase.GetStepHistoryUseCase
import com.example.fit_sentinel.domain.usecase.GetTodaySavedStepsUseCase
import com.example.fit_sentinel.domain.usecase.StartStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.StopStepTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodaySavedStepsUseCase: GetTodaySavedStepsUseCase,
    private val getCurrentSessionStepsUseCase: GetCurrentSessionStepsUseCase,
    private val getStepHistoryUseCase: GetStepHistoryUseCase,
    private val startStepTrackingUseCase: StartStepTrackingUseCase,
    private val stopStepTrackingUseCase: StopStepTrackingUseCase,
    private val calculateStepMetricsUseCase: CalculateStepMetricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeTodaySteps()
        observeStepHistory()
        startStepTracking()
    }

    private fun observeTodaySteps() {
        viewModelScope.launch {
            getCurrentSessionStepsUseCase()
                .collect { currentSessionSteps ->
                    if (_uiState.value.selectedDate == LocalDate.now()) {
                        val savedSteps = getTodaySavedStepsUseCase()
                        val totalStepsToday = savedSteps + currentSessionSteps

                        _uiState.update { it.copy(totalSteps = totalStepsToday) }

                        calculateAndPublishMetrics(totalStepsToday)
                    }
                }
        }
    }

    private fun observeStepHistory() {
        viewModelScope.launch {
            getStepHistoryUseCase()
                .collect { history ->
                    _uiState.update { it.copy(historicalData = history) }
                    updateStepsForSelectedDate(_uiState.value.selectedDate)
                }
        }
    }

    private fun updateStepsForSelectedDate(date: LocalDate) {
        viewModelScope.launch {
            val selectedDayData = _uiState.value.historicalData.find { it.date == date }

            val stepsForSelectedDate = if (date != LocalDate.now()) {
                selectedDayData?.totalSteps ?: 0
            } else {
                getTodaySavedStepsUseCase() + getCurrentSessionStepsUseCase().first()
            }

            _uiState.update { it.copy(totalSteps = stepsForSelectedDate) }

            if (date != LocalDate.now() && selectedDayData != null &&
                selectedDayData.distanceKm != null && selectedDayData.caloriesBurned != null && selectedDayData.estimatedTimeMinutes != null
            ) {
                _uiState.update {
                    it.copy(
                        distance = String.format("%.1f km", selectedDayData.distanceKm),
                        calories = "${selectedDayData.caloriesBurned.roundToInt()} kcal",
                        time = formatMinutesToHoursAndMinutes(selectedDayData.estimatedTimeMinutes)
                    )
                }
            } else {
                calculateAndPublishMetrics(stepsForSelectedDate)
            }
        }
    }

    private fun calculateAndPublishMetrics(steps: Int) {
        viewModelScope.launch {
            val metrics = calculateStepMetricsUseCase(steps)
            _uiState.update {
                it.copy(
                    distance = String.format("%.1f km", metrics.distanceKm),
                    calories = "${metrics.caloriesBurned.roundToInt()} kcal",
                    time = formatMinutesToHoursAndMinutes(metrics.estimatedTimeMinutes)
                )
            }
        }
    }

    private fun formatMinutesToHoursAndMinutes(totalMinutes: Int): String {
        if (totalMinutes < 0) return "0h 0m"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}h ${minutes}m"
    }


    fun onPreviousMonthClicked() {
        val newDate = _uiState.value.selectedDate.minusMonths(1)
        _uiState.update { it.copy(selectedDate = newDate) }
        updateStepsForSelectedDate(newDate)
    }

    fun onNextMonthClicked() {
        val newDate = _uiState.value.selectedDate.plusMonths(1)
        _uiState.update { it.copy(selectedDate = newDate) }
        updateStepsForSelectedDate(newDate)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        updateStepsForSelectedDate(date)
    }

    fun onStepProgressButtonClicked() {
        if (_uiState.value.isRecording) {
            stopStepTracking()
            _uiState.update { it.copy(isRecording = false) }
        } else {
            startStepTracking()
            _uiState.update { it.copy(isRecording = true) }
        }
    }

    private fun startStepTracking() {
        startStepTrackingUseCase()
    }

    private fun stopStepTracking() {
        stopStepTrackingUseCase()
    }

    override fun onCleared() {
        super.onCleared()
        stopStepTracking()
    }
}
