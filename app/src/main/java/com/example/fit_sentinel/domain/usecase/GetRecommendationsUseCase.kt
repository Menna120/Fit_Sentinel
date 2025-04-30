package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.data.remote.dto.AiRequest
import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    operator fun invoke(request: AiRequest): Flow<List<Exercise>> = flow {
        val response = networkRepository.getRecommendations(request)
        if (!response.isSuccess) {
            throw Exception(response.exceptionOrNull()?.message)
        }
        emit(response.getOrNull()?.exercises ?: emptyList())
    }
}
