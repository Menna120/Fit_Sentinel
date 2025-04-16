package com.example.fit_sentinel.di

import android.content.Context
import androidx.room.Room
import com.example.fit_sentinel.data.local.dao.StepDao
import com.example.fit_sentinel.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "step_counter_db"
        )
            // .fallbackToDestructiveMigration() // Use migrations in production
            .build()
    }

    @Provides
    @Singleton
    fun provideStepDao(database: AppDatabase): StepDao {
        return database.stepDao()
    }

}