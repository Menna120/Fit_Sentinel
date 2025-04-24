package com.example.fit_sentinel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "daily_steps")
data class DailyStepsEntity(
    @PrimaryKey val date: LocalDate,
    val totalSteps: Int,
    val lastUpdateTime: Long,
    val distanceKm: Double? = null,
    val caloriesBurned: Double? = null,
    val estimatedTimeMinutes: Int? = null
)