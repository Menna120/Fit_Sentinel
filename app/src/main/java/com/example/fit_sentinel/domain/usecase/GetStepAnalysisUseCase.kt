package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.data.remote.dto.AnalysisRequest
import com.example.fit_sentinel.data.remote.dto.AnalysisResponse
import com.example.fit_sentinel.domain.repository.StepRepository
import javax.inject.Inject

class GetStepAnalysisUseCase @Inject constructor(
    private val stepRepository: StepRepository
) {
    suspend operator fun invoke(request: AnalysisRequest): Result<AnalysisResponse> =
        stepRepository.getStepAnalysis(request)
}
