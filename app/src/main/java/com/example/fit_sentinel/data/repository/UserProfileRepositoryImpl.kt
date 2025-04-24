package com.example.fit_sentinel.data.repository

import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor() : UserProfileRepository {
    // TODO: Implement actual data fetching/saving
    override suspend fun getUserProfile(): UserProfile? {
        // Placeholder data
        return UserProfile(weightKg = 70.0, heightCm = 175.0, age = 30, gender = Gender.Male)
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        // TODO: Implement saving
    }

    override fun getUserProfileFlow(): Flow<UserProfile?> = flow {
        // TODO: Implement flow based on data source
        emit(
            UserProfile(
                weightKg = 70.0,
                heightCm = 175.0,
                age = 30,
                gender = Gender.Male
            )
        ) // Placeholder
    }
}