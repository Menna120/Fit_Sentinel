package com.example.fit_sentinel.domain.usecase

import com.example.fit_sentinel.domain.repository.StepRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentSessionStepsUseCase @Inject constructor(
    private val stepRepository: StepRepository
) {
    operator fun invoke(): Flow<Int> = stepRepository.getCurrentSessionStepsFlow()
}