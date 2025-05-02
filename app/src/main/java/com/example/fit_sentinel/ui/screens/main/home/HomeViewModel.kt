package com.example.fit_sentinel.ui.screens.main.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.usecase.sensor.StartStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.sensor.StopStepTrackingUseCase
import com.example.fit_sentinel.domain.usecase.steps.GetCurrentSessionStepsUseCase
import com.example.fit_sentinel.domain.usecase.steps.GetStepHistoryUseCase
import com.example.fit_sentinel.ui.screens.main.home.components.ActivityRecognitionPermissionTextProvider
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionDialogRequest
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionState
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionTextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    // Dependencies
    private val getCurrentSessionStepsUseCase: GetCurrentSessionStepsUseCase,
    private val getStepHistoryUseCase: GetStepHistoryUseCase,
    private val startStepTrackingUseCase: StartStepTrackingUseCase,
    private val stopStepTrackingUseCase: StopStepTrackingUseCase,
    @ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    private val _activityRecognitionPermissionState = MutableStateFlow(PermissionState.UNKNOWN)

    private val _navigateToSettings = MutableSharedFlow<Unit>()
    private val _requestPermissionEvent = MutableSharedFlow<String>()

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
    val activityRecognitionPermissionState: StateFlow<PermissionState> =
        _activityRecognitionPermissionState.asStateFlow()
    val visiblePermissionDialogQueue = mutableStateListOf<PermissionDialogRequest>()
    val navigateToSettings: SharedFlow<Unit> = _navigateToSettings.asSharedFlow()
    val requestPermissionEvent: SharedFlow<String> = _requestPermissionEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                _uiState.map { it.selectedDate },
                history,
                currentSessionSteps,
                _uiState.map { it.isRecording }
            ) { selectedDate, history, currentSessionSteps, isRecording ->
                val selectedDayData = history.find { it.date == selectedDate }

                val totalSteps: Int
                val distance: String
                val calories: String
                val time: String

                val savedTodaySteps = history.find { it.date == LocalDate.now() }?.totalSteps ?: 0

                if (selectedDate == LocalDate.now()) {
                    totalSteps =
                        if (!isRecording) savedTodaySteps
                        else savedTodaySteps + currentSessionSteps

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

    fun onPermissionStatusDetermined(
        permission: String,
        isGranted: Boolean,
        shouldShowRationale: Boolean
    ) {
        Log.d(
            "HomeViewModel",
            "onPermissionStatusDetermined: $permission, isGranted: $isGranted, shouldShowRationale: $shouldShowRationale"
        )
        when {
            isGranted -> {
                _activityRecognitionPermissionState.value = PermissionState.GRANTED
                if (_uiState.value.isRecording) {
                    startStepTracking()
                }
            }

            shouldShowRationale -> {
                _activityRecognitionPermissionState.value = PermissionState.DENIED_RATIONALE
                if (!visiblePermissionDialogQueue.any { it.permission == permission && !it.isPermanentlyDeclined }) {
                    visiblePermissionDialogQueue.add(
                        PermissionDialogRequest(
                            permission = permission,
                            isPermanentlyDeclined = false,
                            permissionTextProvider = getPermissionTextProvider()
                        )
                    )
                }
            }

            else -> {
                _activityRecognitionPermissionState.value = PermissionState.DENIED_PERMANENT
                if (!visiblePermissionDialogQueue.any { it.permission == permission && it.isPermanentlyDeclined }) {
                    visiblePermissionDialogQueue.add(
                        PermissionDialogRequest(
                            permission = permission,
                            isPermanentlyDeclined = true,
                            permissionTextProvider = getPermissionTextProvider()
                        )
                    )
                }
            }
        }
        visiblePermissionDialogQueue.removeAll { it.permission == permission && _activityRecognitionPermissionState.value == PermissionState.GRANTED }
    }

    fun onStepProgressButtonClicked(activity: Activity, permission: String) {
        Log.d(
            "HomeViewModel",
            "onStepProgressButtonClicked. Current isRecording: ${_uiState.value.isRecording}"
        )
        if (_uiState.value.isRecording) {
            stopStepTracking()
        } else {
            val isGranted = ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            val shouldShowRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

            Log.d(
                "HomeViewModel",
                "Button clicked. Check status: Granted=$isGranted, Rationale=$shouldShowRationale"
            )

            when {
                isGranted -> {
                    _activityRecognitionPermissionState.value = PermissionState.GRANTED
                    startStepTracking()
                }

                shouldShowRationale -> {
                    _activityRecognitionPermissionState.value = PermissionState.DENIED_RATIONALE
                    if (!visiblePermissionDialogQueue.any { it.permission == permission && !it.isPermanentlyDeclined }) {
                        visiblePermissionDialogQueue.add(
                            PermissionDialogRequest(
                                permission = permission,
                                isPermanentlyDeclined = false,
                                permissionTextProvider = getPermissionTextProvider()
                            )
                        )
                    }
                }

                else -> {
                    _activityRecognitionPermissionState.value = PermissionState.REQUESTING
                    viewModelScope.launch {
                        _requestPermissionEvent.emit(permission)
                    }
                }
            }
        }
    }

    fun onPermissionRationaleOkClick(permission: String) {
        dismissDialog()
        _activityRecognitionPermissionState.value =
            PermissionState.REQUESTING // Update state while system dialog shows
        viewModelScope.launch {
            _requestPermissionEvent.emit(permission) // Signal Composable to launch system dialog
        }
    }

    // Called by Composable when user clicks "Settings" on permanent denial dialog
    fun onPermissionPermanentlyDeclinedSettingsClick() {
        viewModelScope.launch {
            dismissDialog() // Dismiss the custom dialog
            _navigateToSettings.emit(Unit) // Signal Composable to navigate
        }
    }

    // Called from Composable when user dismisses any custom permission dialog
    fun dismissDialog() {
        Log.d(
            "HomeViewModel",
            "Dismissing dialog. Queue size before: ${visiblePermissionDialogQueue.size}"
        )
        if (visiblePermissionDialogQueue.isNotEmpty()) {
            val dismissedRequest = visiblePermissionDialogQueue.removeAt(0)
            Log.d(
                "HomeViewModel",
                "Dismissed dialog for permission: ${dismissedRequest.permission}"
            )
            // Update permission state based on what was dismissed
            _activityRecognitionPermissionState.value =
                if (dismissedRequest.isPermanentlyDeclined) {
                    PermissionState.DENIED_PERMANENT
                } else {
                    PermissionState.DENIED_RATIONALE // If rationale dismissed, they still denied it
                }
        }
        Log.d("HomeViewModel", "Queue size after: ${visiblePermissionDialogQueue.size}")

    }

    // Private helper to get the correct text provider for a permission
    private fun getPermissionTextProvider(): PermissionTextProvider {
        return ActivityRecognitionPermissionTextProvider()
    }

    // --- Tracking Control Functions ---

    private fun startStepTracking() {
        _uiState.update { it.copy(isRecording = true) }
        startStepTrackingUseCase()
        Toast.makeText(context, "Session Started", Toast.LENGTH_SHORT).show()
    }

    private fun stopStepTracking() {
        _uiState.update { it.copy(isRecording = false) }
        stopStepTrackingUseCase()
        Toast.makeText(context, "Session Ended", Toast.LENGTH_SHORT).show()
    }

    // --- Date/History Navigation Functions ---

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

    // --- Other Private Helpers ---

    private fun formatMinutesToHoursAndMinutes(totalMinutes: Long?): String {
        if (totalMinutes == null || totalMinutes < 0) return "0h 0m"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}h ${minutes}m"
    }

    // onCleared
    override fun onCleared() {
        super.onCleared()
        stopStepTracking()
    }
}


fun Double.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}