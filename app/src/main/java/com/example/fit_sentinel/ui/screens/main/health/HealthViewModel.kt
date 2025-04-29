package com.example.fit_sentinel.ui.screens.main.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.UserProfile
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.domain.usecase.GetRecommendationsUseCase
import com.example.fit_sentinel.domain.usecase.user_data.CalculateActivityLevelUseCase
import com.example.fit_sentinel.domain.usecase.user_data.GetUserDataUseCase
import com.example.fit_sentinel.domain.usecase.user_data.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val recommendationsUseCase: GetRecommendationsUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val calculateActivityLevelUseCase: CalculateActivityLevelUseCase
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
        getUserDate()
        getHealthData()
    }

    fun saveUserData(weight: Float, weightUnit: WeightUnit, height: Float, heightUnit: HeightUnit) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    userProfile = it.userProfile.copy(
                        weight = weight,
                        weightUnit = weightUnit,
                        height = height,
                        heightUnit = heightUnit,
                    )
                )
            }
            updateUserProfileUseCase(_state.value.userProfile)
        }
    }

    private fun getUserDate() {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                _state.update { it.copy(userProfile = user) }
            }
        }
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
            val activityLevel = calculateActivityLevelUseCase()

            _state.update { it.copy(categoryLabel = activityLevel.toString()) }
        }
    }

    fun updateWeight(weight: Float, weightUnit: WeightUnit) {
        _state.update {
            it.copy(
                userProfile = it.userProfile.copy(
                    weight = weight,
                    weightUnit = weightUnit
                )
            )
        }
        viewModelScope.launch {
            updateUserProfileUseCase(_state.value.userProfile)
            getUserDate()
        }
    }

    fun updateHeight(height: Float, heightUnit: HeightUnit) {
        _state.update {
            it.copy(
                userProfile = it.userProfile.copy(
                    height = height,
                    heightUnit = heightUnit
                )
            )
        }
        viewModelScope.launch {
            updateUserProfileUseCase(_state.value.userProfile)
            getUserDate()
        }
    }
}

data class HealthUiState(
    val recommendations: List<Exercise> = emptyList(),
    val userProfile: UserProfile = UserProfile(),
    val categoryLabel: String = ""
)