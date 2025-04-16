package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.domain.repository.StepRepository
import javax.inject.Inject

class StopStepTrackingUseCase @Inject constructor(
    private val repository: StepRepository
) {
    operator fun invoke() = repository.stopStepTracking()
}