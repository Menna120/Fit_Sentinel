package com.example.fit_sentinel.data.repository

import android.util.Log
import com.example.fit_sentinel.data.remote.ApiService
import com.example.fit_sentinel.data.remote.dto.RecommendationRequest
import com.example.fit_sentinel.data.remote.dto.RecommendationResponse
import com.example.fit_sentinel.domain.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NetworkRepository {
    override suspend fun getRecommendations(request: RecommendationRequest): Result<RecommendationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.recommendations(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "API Error: ${response.code()}"
                    Log.e("StepRepository", "API Analysis Error: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e("StepRepository", "Network/Analysis Exception", e)
                Result.failure(e)
            }
        }
    }
}