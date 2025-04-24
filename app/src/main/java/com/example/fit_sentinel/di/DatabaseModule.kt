package com.example.fit_sentinel.di

import android.content.Context
import androidx.room.Room
import com.example.fit_sentinel.data.local.dao.DailyStepsDao
import com.example.fit_sentinel.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "fit_sentinel_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDailyStepsDao(appDatabase: AppDatabase): DailyStepsDao {
        return appDatabase.dailyStepsDao()
    }
}