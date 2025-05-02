package com.example.fit_sentinel.domain.usecase.steps

import com.example.fit_sentinel.domain.repository.StepRepository
import javax.inject.Inject

class SaveStepsUseCase @Inject constructor(
    private val stepRepository: StepRepository
) {
    suspend operator fun invoke(steps: Int): Unit = stepRepository.saveSteps(steps)
}