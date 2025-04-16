package com.example.fit_sentinel.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fit_sentinel.data.local.dao.StepDao
import com.example.fit_sentinel.data.local.entity.StepDataEntity

@Database(entities = [StepDataEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stepDao(): StepDao
}