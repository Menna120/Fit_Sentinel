package com.example.fit_sentinel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit

@Entity(tableName = "user_data")
data class UserDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val weightUnit: WeightUnit,
    val height: Int,
    val heightUnit: HeightUnit,
    val activityLevel: String,
    val goalWeight: Float,
    val chronicDiseases: String,
    val isSmoker: Boolean,
    val bmi: Float,
    val bmiCategory: String
)
