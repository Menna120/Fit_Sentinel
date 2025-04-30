package com.example.fit_sentinel.data.repository

import android.util.Log
import com.example.fit_sentinel.data.remote.ApiService
import com.example.fit_sentinel.data.remote.dto.AiRequest
import com.example.fit_sentinel.data.remote.dto.ResultApi
import com.example.fit_sentinel.domain.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NetworkRepository {
    override suspend fun getRecommendations(request: AiRequest): Result<ResultApi> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.recommendations(request)
                if (response.isSuccessful && response.body() != null) {
                    Log.d(
                        "NetworkRepositoryImpl",
                        Result.success(response.body()!!.result).toString()
                    )
                    Result.success(response.body()!!.result)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "API Error: ${response.code()}"
                    Log.e("NetworkRepositoryImpl", "API Analysis Error: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Network/Analysis Exception", e)
                Result.failure(e)
            }
        }
    }
}