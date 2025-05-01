package com.example.fit_sentinel.domain.repository

import com.example.fit_sentinel.domain.model.StepMetrics

interface StepMetricsRepository {
    suspend fun getStepMetric(steps: Int, durationMillis: Long? = null): StepMetrics
}