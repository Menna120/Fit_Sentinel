package com.example.fit_sentinel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyStepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailySteps(dailySteps: DailyStepsEntity)

    @Query("SELECT * FROM daily_steps WHERE date = :date")
    suspend fun getDailySteps(date: LocalDate): DailyStepsEntity?

    @Query("SELECT * FROM daily_steps ORDER BY date DESC")
    fun getAllDailySteps(): Flow<List<DailyStepsEntity>>

    @Query("DELETE FROM daily_steps WHERE date < :dateThreshold")
    suspend fun deleteOldData(dateThreshold: LocalDate)
}