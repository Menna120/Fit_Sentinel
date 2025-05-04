package com.example.fit_sentinel.ui.screens.main.home

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.usecase.sensor.StartStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.sensor.StopStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.steps.GetCurrentSessionStepsUseCase
import com.example.fit_sentinel.domain.usecase.steps.GetStepHistoryUseCase
import com.example.fit_sentinel.ui.screens.main.home.components.ActivityRecognitionPermissionTextProvider
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionDialogRequest
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionTextProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val stopStepTrackingUseCase: StopStepTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    private val _currentPermissionDialogRequest = MutableStateFlow<PermissionDialogRequest?>(null)
    private val _navigateToSettings = MutableSharedFlow<Unit>()
    private val _toastEvent = MutableSharedFlow<HomeToastEvent>()

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

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    val currentPermissionDialogRequest: StateFlow<PermissionDialogRequest?> =
        _currentPermissionDialogRequest.asStateFlow()
    val navigateToSettings: SharedFlow<Unit> = _navigateToSettings.asSharedFlow()
    val toastEvent: SharedFlow<HomeToastEvent> = _toastEvent.asSharedFlow()

    private val activityRecognitionPermissionTextProvider: PermissionTextProvider =
        ActivityRecognitionPermissionTextProvider()

    init {
        viewModelScope.launch {
            combine(
                _uiState.map { it.selectedDate },
                history,
                currentSessionSteps,
                _uiState.map { it.isRecording }
            ) { selectedDate, history, currentSessionSteps, isRecording ->
                val selectedDayData = history.find { it.date == selectedDate }

                val savedTodaySteps = history.find { it.date == LocalDate.now() }?.totalSteps ?: 0

                val totalSteps = if (selectedDate == LocalDate.now()) {
                    if (!isRecording) savedTodaySteps
                    else savedTodaySteps + currentSessionSteps
                } else selectedDayData?.totalSteps ?: 0

                val distance = selectedDayData?.distanceKm?.format(1) ?: "0.0"
                val calories = selectedDayData?.caloriesBurned?.roundToInt()?.toString() ?: "0"
                val time = formatMinutesToHoursAndMinutes(selectedDayData?.estimatedTimeMinutes)

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

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalPermissionsApi::class)
    fun onStepProgressButtonClicked(currentPermissionStatus: PermissionStatus) {
        if (_uiState.value.isRecording) {
            stopStepTracking()
        } else {
            when (currentPermissionStatus) {
                PermissionStatus.Granted -> {
                    startStepTracking()
                }

                is PermissionStatus.Denied -> {
                    val isPermanentlyDeclined = !currentPermissionStatus.shouldShowRationale
                    _currentPermissionDialogRequest.value = PermissionDialogRequest(
                        permission = Manifest.permission.ACTIVITY_RECOGNITION,
                        isPermanentlyDeclined = isPermanentlyDeclined,
                        permissionTextProvider = activityRecognitionPermissionTextProvider
                    )
                }
            }
        }
    }

    fun onPermissionRationaleOkClick() = dismissDialog()

    fun onPermissionPermanentlyDeclinedSettingsClick() {
        viewModelScope.launch {
            dismissDialog()
            _navigateToSettings.emit(Unit)
        }
    }

    fun dismissDialog() {
        _currentPermissionDialogRequest.value = null
    }

    private fun startStepTracking() {
        startStepTrackingUseCase()
        _uiState.update { it.copy(isRecording = true) }
        viewModelScope.launch {
            _toastEvent.emit(HomeToastEvent.SessionStarted)
        }
    }

    private fun stopStepTracking() {
        stopStepTrackingUseCase()
        _uiState.update { it.copy(isRecording = false) }
        viewModelScope.launch {
            _toastEvent.emit(HomeToastEvent.SessionEnded)
        }
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

    private fun formatMinutesToHoursAndMinutes(totalMinutes: Long?): String {
        if (totalMinutes == null || totalMinutes < 0) return "0h 0m"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}h ${minutes}m"
    }

    override fun onCleared() {
        super.onCleared()
        stopStepTracking()
    }
}


fun Double.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}