package com.example.fit_sentinel.data.remote

import com.example.fit_sentinel.data.remote.dto.RecommendationRequest
import com.example.fit_sentinel.data.remote.dto.RecommendationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/predict")
    suspend fun recommendations(
        @Body request: RecommendationRequest
    ): Response<RecommendationResponse>
}