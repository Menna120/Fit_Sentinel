package com.example.fit_sentinel.ui.screens.main.health.camera

import androidx.camera.core.ExperimentalGetImage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.atan2

// ViewModel to handle pose data and exercise counting
@ExperimentalGetImage
class ExerciseCounterViewModel : ViewModel() {

    private val poseAnalyzer = MLKitPoseAnalyzer() // Create an instance of your analyzer

    private val _latestPose = MutableStateFlow<Pose?>(null)
    val latestPose: StateFlow<Pose?> = _latestPose.asStateFlow()

    private val _exerciseCount = MutableStateFlow(0)
    val exerciseCount: StateFlow<Int> = _exerciseCount.asStateFlow()

    // State to track the exercise state (e.g., "up" or "down" for squats)
    private val _exerciseState = MutableStateFlow<ExerciseState>(ExerciseState.UNKNOWN)
    val exerciseState: StateFlow<ExerciseState> = _exerciseState.asStateFlow()


    init {
        // Collect pose results from the analyzer
        viewModelScope.launch {
            poseAnalyzer.poseResults.collect { pose ->
                _latestPose.value = pose // Update the latest pose
                analyzePoseForExercise(pose) // Analyze the pose for counting
            }
        }
    }

    // Function to get the FrameAnalyzer instance for CameraPreview
    fun getFrameAnalyzer(): MLKitPoseAnalyzer {
        return poseAnalyzer
    }

    // --- Exercise Counting Logic (Example: Simple Squat Counter) ---
    // This is a basic example. More robust logic is needed for real-world applications.
    private fun analyzePoseForExercise(pose: Pose) {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

        if (leftHip != null && leftKnee != null && leftAnkle != null &&
            rightHip != null && rightKnee != null && rightAnkle != null
        ) {

            // Calculate the angle at the left and right knees
            val leftKneeAngle = getAngle(leftHip, leftKnee, leftAnkle)
            val rightKneeAngle = getAngle(rightHip, rightKnee, rightAnkle)

            // Use the average angle for simplicity
            val averageKneeAngle = (leftKneeAngle + rightKneeAngle) / 2

            // Define thresholds for squat detection (these will need tuning)
            val squatDownThreshold = 100.0 // Angle when in squat position
            val squatUpThreshold = 160.0 // Angle when standing up

            when (_exerciseState.value) {
                ExerciseState.UNKNOWN -> {
                    // If unknown, check if they are in the up or down position
                    if (averageKneeAngle > squatUpThreshold) {
                        _exerciseState.value = ExerciseState.UP
                    } else if (averageKneeAngle < squatDownThreshold) {
                        _exerciseState.value = ExerciseState.DOWN
                    }
                }

                ExerciseState.UP -> {
                    // If in UP state, check if they move to the DOWN state
                    if (averageKneeAngle < squatDownThreshold) {
                        _exerciseState.value = ExerciseState.DOWN
                    }
                }

                ExerciseState.DOWN -> {
                    // If in DOWN state, check if they move back to the UP state
                    if (averageKneeAngle > squatUpThreshold) {
                        _exerciseState.value = ExerciseState.UP
                        _exerciseCount.update { it + 1 } // Increment count when returning to UP
                    }
                }
            }
        }
    }

    // Helper function to calculate angle between three points (landmarks)
    private fun getAngle(first: PoseLandmark, mid: PoseLandmark, last: PoseLandmark): Double {
        var angle = Math.toDegrees(
            atan2(
                (last.position.y - mid.position.y).toDouble(),
                (last.position.x - mid.position.x).toDouble()
            ) - atan2(
                (first.position.y - mid.position.y).toDouble(),
                (first.position.x - mid.position.x).toDouble()
            )
        )
        if (angle < 0) {
            angle += 360
        }
        return angle
    }

    // Enum to represent the state of the exercise
    enum class ExerciseState {
        UNKNOWN, UP, DOWN
    }

    // Remember to close the analyzer when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        poseAnalyzer.closeDetector()
    }
}
