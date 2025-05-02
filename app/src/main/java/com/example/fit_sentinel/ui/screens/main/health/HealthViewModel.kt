package com.example.fit_sentinel.ui.screens.main.health

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_sentinel.data.remote.dto.AiRequest
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.domain.usecase.ai.GetRecommendationsUseCase
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
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val recommendationsUseCase: GetRecommendationsUseCase,
    private val calculateActivityLevelUseCase: CalculateActivityLevelUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HealthUiState())
    val state: StateFlow<HealthUiState> = _state.asStateFlow()

    init {
        updateUiState()
        getHealthData()
        getRecommendations()
    }

    fun saveUserData(weight: Float, weightUnit: WeightUnit, height: Int, heightUnit: HeightUnit) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    userProfile = it.userProfile.copy(
                        weight = weight,
                        weightUnit = weightUnit,
                        height = height,
                        heightUnit = heightUnit

                    )
                )
            }
            updateUserProfileUseCase(_state.value.userProfile)
        }
    }

    private fun updateUiState() {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                _state.update { it.copy(userProfile = user) }
            }
        }
    }

    fun getRecommendations() {
        viewModelScope.launch {
            recommendationsUseCase(
                AiRequest(
                    weight = _state.value.userProfile.weight,
                    height = _state.value.userProfile.height,
                    age = _state.value.userProfile.age,
                    activityLevel = _state.value.userProfile.activityLevel,
                    bodyGoal = _state.value.userProfile.goalWeight
                )
            ).collect { result ->
                Log.d("HealthViewModel", result.toString())
                _state.update {
                    it.copy(
                        recommendations = result
                    )
                }
            }
        }
//        viewModelScope.launch {
//            _state.update {
//                it.copy(
//                    recommendations = listOf(
//                        Exercise(
//                            exerciseName = "Barbell Squats",
//                            sets = 3,
//                            reps = 12,
//                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
//                        ),
//                        Exercise(
//                            exerciseName = "Barbell Squats",
//                            sets = 3,
//                            reps = 12,
//                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
//                        ),
//                        Exercise(
//                            exerciseName = "Barbell Squats",
//                            sets = 3,
//                            reps = 12,
//                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
//                        ),
//                        Exercise(
//                            exerciseName = "Barbell Squats",
//                            sets = 3,
//                            reps = 12,
//                            description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
//                        ),
//                    )
//                )
//            }
//        }
    }

    private fun getHealthData() {
        viewModelScope.launch {
            val activityLevel = calculateActivityLevelUseCase()
            _state.update {
                it.copy(
                    userProfile = _state.value.userProfile.copy(activityLevel = activityLevel.toString())
                )
            }
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
            updateUiState()
        }
    }

    fun updateHeight(height: Int, heightUnit: HeightUnit) {
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
            updateUiState()
        }
    }
}