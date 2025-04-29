package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DailyStepsRepository {
    fun getAllDailySteps(): Flow<List<DailyStepsEntity>>
    suspend fun insertOrUpdateDailySteps(dailySteps: DailyStepsEntity)
    suspend fun getDailySteps(date: LocalDate): DailyStepsEntity?
    suspend fun deleteOldData(dateThreshold: LocalDate)
    fun getDailyStepsForDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DailyStepsEntity>>

    suspend fun getStepsBetween(startDate: LocalDate, endDate: LocalDate): List<DailyStepsEntity>

}
