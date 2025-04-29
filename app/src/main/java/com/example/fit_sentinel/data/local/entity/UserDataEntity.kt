package com.example.fit_sentinel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fit_sentinel.data.model.Gender

@Entity(tableName = "user_data")
data class UserDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val height: Float,
    val activityLevel: String,
    val goalWeight: Float,
    val chronicDiseases: String,
    val isSmoker: Boolean,
    val bmi: Float = 0f
)
