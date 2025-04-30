package com.example.fit_sentinel.domain.usecase.user_data

import com.example.fit_sentinel.domain.mapper.toUserDataEntity
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.repository.UserDataRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(userProfile: UserProfile) {
        repository.saveUserData(userProfile.toUserDataEntity())
    }
}