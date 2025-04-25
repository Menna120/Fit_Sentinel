package com.example.fit_sentinel.ui.screens.main.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.domain.usecase.GetWeeklyStepsUseCase
import com.example.fit_sentinel.ui.screens.main.reports.model.DailyStepData
import com.example.fit_sentinel.ui.screens.main.reports.model.DayProgress
import com.example.fit_sentinel.ui.screens.main.reports.model.WeeklyReportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getWeeklyStepsUseCase: GetWeeklyStepsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeeklyReportState())
    val state: StateFlow<WeeklyReportState> = _state.asStateFlow()

    fun loadWeeklyData(startDate: LocalDate, endDate: LocalDate) {
        _state.update { it.copy(startDate = startDate, endDate = endDate) }

        viewModelScope.launch {
            getWeeklyStepsUseCase(startDate, endDate)
                .collect { fetchedData ->
                    val dailyProgress = mutableListOf<DayProgress>()
                    val dailyStepData = mutableListOf<DailyStepData>()

                    var totalSteps = 0.0
                    var totalMinutes = 0
                    var totalCals = 0.0
                    var totalDistance = 0.0

                    var currentDate = startDate
                    while (!currentDate.isAfter(endDate)) {
                        val entityForDate = fetchedData.find { it.date == currentDate }

                        val progress =
                            (entityForDate?.totalSteps?.toFloat() ?: 0f) / _state.value.targetSteps
                        dailyProgress.add(
                            DayProgress(
                                dayOfMonth = currentDate.dayOfMonth,
                                progress = progress.coerceIn(0f, 1f)
                            )
                        )

                        dailyStepData.add(
                            DailyStepData(
                                dayLabel = entityForDate?.caloriesBurned ?: 0.0,
                                steps = entityForDate?.totalSteps?.toDouble() ?: 0.0
                            )
                        )

                        entityForDate?.let {
                            totalSteps += it.totalSteps
                            totalMinutes += it.estimatedTimeMinutes ?: 0
                            totalCals += it.caloriesBurned ?: 0.0
                            totalDistance += it.distanceKm ?: 0.0
                        }

                        currentDate = currentDate.plusDays(1)
                    }

                    val totalTimeFormatted =
                        if (totalMinutes > 0) "${totalMinutes / 60}h ${totalMinutes % 60}m" else "0"
                    val totalCaloriesFormatted =
                        if (totalCals > 0) "${totalCals.toInt()} kcal" else "0"
                    val totalDistanceFormatted =
                        if (totalDistance > 0) "%.1f km".format(totalDistance) else "0"

                    _state.update {
                        it.copy(
                            weeklyStepsData = fetchedData,
                            dailyProgressList = dailyProgress,
                            dailyStepDataList = dailyStepData,
                            totalStepsThisWeek = totalSteps,
                            totalTimeThisWeek = totalTimeFormatted,
                            totalCaloriesThisWeek = totalCaloriesFormatted,
                            totalDistanceThisWeek = totalDistanceFormatted
                        )
                    }
                }
        }
    }

    fun goToPreviousWeek() {
        val newStartDate = _state.value.startDate.minusWeeks(1)
        val newEndDate = _state.value.endDate.minusWeeks(1)
        loadWeeklyData(newStartDate, newEndDate)
    }

    fun goToNextWeek() {
        val newStartDate = _state.value.startDate.plusWeeks(1)
        val newEndDate = _state.value.endDate.plusWeeks(1)
        loadWeeklyData(newStartDate, newEndDate)
    }
}
