package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.local.entity.UserDataEntity
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    fun getUserData(): Flow<UserDataEntity?>
    suspend fun getUserById(userId: Int): UserDataEntity?
    suspend fun saveUserData(userData: UserDataEntity)
    suspend fun updateUserData(userData: UserDataEntity)
    suspend fun deleteUserData(userData: UserDataEntity)
    suspend fun deleteAllUsers()
    suspend fun getUserCount(): Int
}