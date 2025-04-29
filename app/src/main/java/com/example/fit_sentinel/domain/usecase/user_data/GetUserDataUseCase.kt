package com.example.fit_sentinel.domain.usecase.user_data

import com.example.fit_sentinel.data.model.Gender
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: UserDataRepository
) {
    operator fun invoke(): Flow<UserProfile> {
        return repository.getUserData().map {
            UserProfile(
                name = it?.name ?: "",
                gender = it?.gender ?: Gender.PREFER_NOT_TO_SAY,
                age = it?.age ?: 0,
                height = it?.height ?: 0f,
                weight = it?.weight ?: 0f,
                activityLevel = it?.activityLevel ?: "",
                goalWeight = it?.goalWeight ?: 0f,
                chronicDiseases = it?.chronicDiseases ?: "",
                isSmoker = it?.isSmoker == true,
                bmi = it?.bmi ?: 0f,
            )
        }
    }
}