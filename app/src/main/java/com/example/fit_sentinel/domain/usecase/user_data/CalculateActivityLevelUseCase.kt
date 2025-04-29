package com.example.fit_sentinel.domain.usecase.user_data

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.data.model.ActivityLevel
import com.example.fit_sentinel.domain.repository.DailyStepsRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CalculateActivityLevelUseCase @Inject constructor(
    private val stepsRepository: DailyStepsRepository
) {
    suspend operator fun invoke(): ActivityLevel {
        val stepsData: List<DailyStepsEntity>? = stepsRepository.getAllDailySteps().firstOrNull()

        if (stepsData.isNullOrEmpty()) {
            return ActivityLevel.UNKNOWN
        }

        val totalSteps = stepsData.sumOf { it.totalSteps }

        val numberOfDays = stepsData.size

        val averageSteps = totalSteps.toDouble() / numberOfDays

        return when {
            averageSteps < 5000 -> ActivityLevel.SEDENTARY
            averageSteps < 7500 -> ActivityLevel.LIGHTLY_ACTIVE
            averageSteps < 10000 -> ActivityLevel.FAIRLY_ACTIVE
            averageSteps < 12500 -> ActivityLevel.ACTIVE
            else -> ActivityLevel.VERY_ACTIVE
        }
    }
}