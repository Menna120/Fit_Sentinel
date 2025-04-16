package com.example.fit_sentinel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fit_sentinel.data.local.entity.StepDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepData(stepData: StepDataEntity)

    // Get total steps for a specific day
    @Query("SELECT SUM(steps) FROM step_data WHERE timestamp >= :startOfDay AND timestamp < :endOfDay")
    suspend fun getTotalStepsForDay(startOfDay: Long, endOfDay: Long): Int? // Nullable if no data

    // Get all step data entries
    @Query("SELECT * FROM step_data ORDER BY timestamp DESC")
    fun getAllStepData(): Flow<List<StepDataEntity>>

    // Get the most recent step entry
    @Query("SELECT * FROM step_data ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestStepData(): StepDataEntity?

    // Delete old data
    @Query("DELETE FROM step_data WHERE timestamp < :timestamp")
    suspend fun deleteOldData(timestamp: Long)
}
