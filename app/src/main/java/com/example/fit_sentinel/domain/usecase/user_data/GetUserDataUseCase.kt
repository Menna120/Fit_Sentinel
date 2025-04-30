package com.example.fit_sentinel.domain.usecase.user_data

import com.example.fit_sentinel.domain.mapper.toUserProfile
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: UserDataRepository
) {
    operator fun invoke(): Flow<UserProfile> {
        return repository.getUserData().map { it.toUserProfile() }
    }
}