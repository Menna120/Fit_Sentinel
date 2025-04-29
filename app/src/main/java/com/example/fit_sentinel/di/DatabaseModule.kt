package com.example.fit_sentinel.di

import android.content.Context
import androidx.room.Room
import com.example.fit_sentinel.data.local.dao.DailyStepsDao
import com.example.fit_sentinel.data.local.db.StepCounterDatabase
import com.example.fit_sentinel.data.local.db.UserDataBase
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
    fun provideStepCounterDatabase(@ApplicationContext context: Context): StepCounterDatabase {
        return Room.databaseBuilder(
            context,
            StepCounterDatabase::class.java,
            "fit_sentinel_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDailyStepsDao(stepCounterDatabase: StepCounterDatabase): DailyStepsDao {
        return stepCounterDatabase.dailyStepsDao()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDataBase {
        return Room.databaseBuilder(
            context,
            UserDataBase::class.java,
            "user_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(userDataBase: UserDataBase): com.example.fit_sentinel.data.local.dao.UserDao {
        return userDataBase.userDao()
    }


}