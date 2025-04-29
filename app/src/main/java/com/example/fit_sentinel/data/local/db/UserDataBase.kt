package com.example.fit_sentinel.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fit_sentinel.data.local.dao.UserDao
import com.example.fit_sentinel.data.local.entity.UserDataEntity

@Database(entities = [UserDataEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}