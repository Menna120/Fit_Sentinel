package com.example.fit_sentinel.domain.usecase.steps

import com.example.fit_sentinel.data.local.entity.DailyStepsEntity
import com.example.fit_sentinel.domain.repository.StepRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStepHistoryUseCase @Inject constructor(
    private val stepRepository: StepRepository
) {
    operator fun invoke(): Flow<List<DailyStepsEntity>> = stepRepository.getStepHistory()
}