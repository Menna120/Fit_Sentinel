package com.example.fit_sentinel.data.remote

import com.example.fit_sentinel.data.remote.dto.AnalysisRequest
import com.example.fit_sentinel.data.remote.dto.AnalysisResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("analyze")
    suspend fun analyzeSteps(@Body request: AnalysisRequest): Response<AnalysisResponse>
}