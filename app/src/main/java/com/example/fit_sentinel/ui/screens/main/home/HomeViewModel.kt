package com.example.fit_sentinel.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.usecase.sensor.StartStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.sensor.StopStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.steps.GetCurrentSessionStepsUseCase
import com.example.fit_sentinel.domain.usecase.steps.GetStepHistoryUseCase
import com.example.fit_sentinel.domain.usecase.steps.SaveStepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrentSessionStepsUseCase: GetCurrentSessionStepsUseCase,
    getStepHistoryUseCase: GetStepHistoryUseCase,
    private val startStepTrackingUseCase: StartStepTrackingUseCase,
    private val stopStepTrackingUseCase: StopStepTrackingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val currentSessionSteps: StateFlow<Int> = getCurrentSessionStepsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private val history: StateFlow<List<DailyStepsEntity>> = getStepHistoryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            combine(
                _uiState.map { it.selectedDate },
                history,
                currentSessionSteps,
            ) { selectedDate, history, currentSessionSteps ->
                val selectedDayData = history.find { it.date == selectedDate }

                val totalSteps: Int
                val distance: String
                val calories: String
                val time: String

                if (selectedDate == LocalDate.now()) {
                    val savedTodaySteps =
                        history.find { it.date == LocalDate.now() }?.totalSteps ?: 0
                    totalSteps =
                        if (savedTodaySteps == _uiState.value.totalSteps && !_uiState.value.isRecording) savedTodaySteps else savedTodaySteps + currentSessionSteps

                    distance = selectedDayData?.distanceKm?.format(1) ?: "0.0"
                    calories = selectedDayData?.caloriesBurned?.roundToInt()?.toString() ?: "0"
                    time = formatMinutesToHoursAndMinutes(selectedDayData?.estimatedTimeMinutes)

                } else {
                    totalSteps = selectedDayData?.totalSteps ?: 0
                    distance = selectedDayData?.distanceKm?.format(1) ?: "0.0"
                    calories = selectedDayData?.caloriesBurned?.roundToInt()?.toString() ?: "0"
                    time = formatMinutesToHoursAndMinutes(selectedDayData?.estimatedTimeMinutes)
                }

                _uiState.update {
                    it.copy(
                        totalSteps = totalSteps,
                        historicalData = history,
                        distance = distance,
                        calories = calories,
                        time = time
                    )
                }
            }
                .collect { }
        }
    }

    private fun formatMinutesToHoursAndMinutes(totalMinutes: Long?): String {
        if (totalMinutes == null || totalMinutes < 0) return "0h 0m"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}h ${minutes}m"
    }

    fun onPreviousWeekClicked() {
        val newDate = _uiState.value.selectedDate
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
            .minusWeeks(1)
        _uiState.update { it.copy(selectedDate = newDate) }
    }

    fun onNextWeekClicked() {
        val newDate = _uiState.value.selectedDate
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
            .plusWeeks(1)
        _uiState.update { it.copy(selectedDate = newDate) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
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

fun Double.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}