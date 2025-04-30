package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.data.remote.dto.AiRequest
import com.example.fit_sentinel.data.remote.dto.ResultApi

interface NetworkRepository {
    suspend fun getRecommendations(request: AiRequest): Result<ResultApi>
}