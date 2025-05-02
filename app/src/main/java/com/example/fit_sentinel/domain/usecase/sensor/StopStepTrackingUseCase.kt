package com.example.fit_sentinel.domain.usecase.sensor

import com.example.fit_sentinel.domain.repository.StepRepository
import javax.inject.Inject

class StopStepTrackingUseCase @Inject constructor(
    private val stepRepository: StepRepository
) {
    operator fun invoke() = stepRepository.stopStepTracking()
}