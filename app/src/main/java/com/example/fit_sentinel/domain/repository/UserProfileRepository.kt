package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    suspend fun getUserProfile(): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
    fun getUserProfileFlow(): Flow<UserProfile?>
}