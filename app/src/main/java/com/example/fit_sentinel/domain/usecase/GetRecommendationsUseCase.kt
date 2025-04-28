package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.data.remote.dto.RecommendationRequest
import com.example.fit_sentinel.domain.repository.NetworkRepository
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(request: RecommendationRequest): List<Exercise> {
        val response = networkRepository.getRecommendations(request)
        if (!response.isSuccess) {
            throw Exception("Failed to fetch recommendations")
        }
        return response.getOrNull()?.exercises ?: emptyList()
    }
}
