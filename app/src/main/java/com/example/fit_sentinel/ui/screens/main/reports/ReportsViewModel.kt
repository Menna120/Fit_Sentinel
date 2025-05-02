package com.example.fit_sentinel.ui.screens.main.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.usecase.steps.GetWeeklyStepsUseCase
import com.example.fit_sentinel.ui.screens.main.reports.model.DailyStepData
import com.example.fit_sentinel.ui.screens.main.reports.model.DayProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getWeeklyStepsUseCase: GetWeeklyStepsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeeklyReportState())
    val state: StateFlow<WeeklyReportState> = _state.asStateFlow()

    init {
        val today = LocalDate.now()
        val initialStartDate = today.minusDays(today.dayOfWeek.value.toLong() - 1)
        val initialEndDate = initialStartDate.plusDays(6)
        _state.update {
            it.copy(
                startDate = initialStartDate,
                endDate = initialEndDate
            )
        }
    }

    fun loadWeeklyData(startDate: LocalDate, endDate: LocalDate) {
        _state.update { it.copy(startDate = startDate, endDate = endDate, isLoading = true) }

        viewModelScope.launch {
            getWeeklyStepsUseCase(startDate, endDate)
                .collectLatest { fetchedData ->
                    val dailyProgress = mutableListOf<DayProgress>()
                    val dailyStepData = mutableListOf<DailyStepData>()

                    var totalSteps = 0.0
                    var totalMinutes = 0L
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
                            totalMinutes += it.estimatedTimeMinutes ?: 0L
                            totalCals += it.caloriesBurned ?: 0.0
                            totalDistance += it.distanceKm ?: 0.0
                        }

                        currentDate = currentDate.plusDays(1)
                    }

                    val totalTimeFormatted =
                        if (totalMinutes > 0L) "${totalMinutes / 60}h ${totalMinutes % 60}m" else "0h 0m"
                    val totalCaloriesFormatted =
                        if (totalCals > 0) "${totalCals.roundToInt()} kcal" else "0 kcal"
                    val totalDistanceFormatted =
                        if (totalDistance > 0) "%.1f km".format(totalDistance) else "0.0 km"

                    _state.update {
                        it.copy(
                            weeklyStepsData = fetchedData,
                            dailyProgressList = dailyProgress,
                            dailyStepDataList = dailyStepData,
                            totalStepsThisWeek = totalSteps,
                            totalTimeThisWeek = totalTimeFormatted,
                            totalCaloriesThisWeek = totalCaloriesFormatted,
                            totalDistanceThisWeek = totalDistanceFormatted,
                            isLoading = false
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

    data class WeeklyReportState(
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val targetSteps: Int = 10000,
        val weeklyStepsData: List<DailyStepsEntity> = emptyList(),
        val dailyProgressList: List<DayProgress> = emptyList(),
        val dailyStepDataList: List<DailyStepData> = emptyList(),
        val totalStepsThisWeek: Double = 0.0,
        val totalTimeThisWeek: String = "0h 0m",
        val totalCaloriesThisWeek: String = "0 kcal",
        val totalDistanceThisWeek: String = "0.0 km",
        val isLoading: Boolean = false
    )
}