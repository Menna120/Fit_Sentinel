package com.example.fit_sentinel.data.repository

import com.example.fit_sentinel.data.local.dao.DailyStepsDao
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.repository.DailyStepsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DailyStepsRepositoryImpl @Inject constructor(
    private val dailyStepsDao: DailyStepsDao
) : DailyStepsRepository {

    override fun getAllDailySteps(): Flow<List<DailyStepsEntity>> {
        return dailyStepsDao.getAllDailySteps()
    }

    override suspend fun insertOrUpdateDailySteps(dailySteps: DailyStepsEntity) {
        dailyStepsDao.insertOrUpdateDailySteps(dailySteps)
    }

    override suspend fun getDailySteps(date: LocalDate): DailyStepsEntity? {
        return dailyStepsDao.getDailySteps(date)
    }

    override suspend fun getDailyStepsForDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyStepsEntity> {
        return dailyStepsDao.getStepsBetween(startDate, endDate)
    }
}
