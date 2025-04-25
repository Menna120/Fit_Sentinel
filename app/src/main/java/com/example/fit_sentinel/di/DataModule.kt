package com.example.fit_sentinel.di

import com.example.fit_sentinel.data.local.dao.DailyStepsDao
import com.example.fit_sentinel.data.repository.DailyStepsRepositoryImpl
import com.example.fit_sentinel.domain.repository.DailyStepsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDailyStepsRepository(dailyStepsDao: DailyStepsDao): DailyStepsRepository {
        return DailyStepsRepositoryImpl(dailyStepsDao)
    }
}
