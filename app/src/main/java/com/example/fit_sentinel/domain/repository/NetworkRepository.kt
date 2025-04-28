package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.remote.dto.RecommendationRequest
import com.example.fit_sentinel.data.remote.dto.RecommendationResponse

interface NetworkRepository {
    suspend fun getRecommendations(request: RecommendationRequest): Result<RecommendationResponse>
}