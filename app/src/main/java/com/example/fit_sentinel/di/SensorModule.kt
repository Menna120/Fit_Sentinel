package com.example.fit_sentinel.di

import android.content.Context
import android.hardware.SensorManager
import com.example.fit_sentinel.data.repository.StepSensorManagerImpl
import com.example.fit_sentinel.domain.repository.StepSensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @Provides
    @Singleton
    fun provideStepSensorManager(sensorManager: SensorManager): StepSensorManager {
        return StepSensorManagerImpl(sensorManager)
    }

}