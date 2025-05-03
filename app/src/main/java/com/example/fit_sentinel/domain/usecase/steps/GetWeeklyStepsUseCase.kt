package com.example.fit_sentinel.domain.usecase.steps

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.repository.DailyStepsRepository
import java.time.LocalDate
import javax.inject.Inject

class GetWeeklyStepsUseCase @Inject constructor(
    private val repository: DailyStepsRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): List<DailyStepsEntity> =
        repository.getDailyStepsForDateRange(startDate, endDate)
}
