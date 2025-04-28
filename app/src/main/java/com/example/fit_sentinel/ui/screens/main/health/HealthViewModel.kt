package com.example.fit_sentinel.ui.screens.main.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.domain.usecase.GetRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val recommendationsUseCase: GetRecommendationsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HealthUiState())
    val state: StateFlow<HealthUiState> = _state.asStateFlow()

    init {
//        viewModelScope.launch {
//            _recommendations.value = recommendationsUseCase(
//                RecommendationRequest(
//                    current_weight = "70 kg",
//                    height = "160 cm",
//                    age = "23",
//                    activity_level = "Normal",
//                    goal = "60 kg"
//                )
//            )
//        }
    }

    init {
        getRecommendations()
        getHealthData()
    }

    private fun getRecommendations() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    recommendations = listOf(
                        Exercise(
                            exercise_name = "Barbell Squats",
                            sets = 3,
                            reps = 12,
                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                        ),
                        Exercise(
                            exercise_name = "Barbell Squats",
                            sets = 3,
                            reps = 12,
                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                        ),
                        Exercise(
                            exercise_name = "Barbell Squats",
                            sets = 3,
                            reps = 12,
                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                        ),
                        Exercise(
                            exercise_name = "Barbell Squats",
                            sets = 3,
                            reps = 12,
                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                        ),
                    )
                )
            }
        }
    }

    private fun getHealthData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    bmi = 22.5f,
                    categoryLabel = "Normal"
                )
            }
        }
    }
}

data class HealthUiState(
    val recommendations: List<Exercise> = emptyList(),
    val bmi: Float = 0f,
    val categoryLabel: String = ""
)