package com.example.fit_sentinel.di

import com.example.fit_sentinel.data.local.dao.StepDao
import com.example.fit_sentinel.data.remote.ApiService
import com.example.fit_sentinel.data.repository.StepRepositoryImpl
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.domain.repository.StepSensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideStepRepository(
        stepDao: StepDao,
        apiService: ApiService,
        stepSensorManager: StepSensorManager
    ): StepRepository {
        return StepRepositoryImpl(stepDao, apiService, stepSensorManager)
    }

}