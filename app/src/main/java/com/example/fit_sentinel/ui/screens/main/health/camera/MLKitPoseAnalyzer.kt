package com.example.fit_sentinel.ui.screens.main.health.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

// Analyzer that uses ML Kit Pose Detection
@ExperimentalGetImage
class MLKitPoseAnalyzer : FrameAnalyzer {

    // Configure the pose detector options (e.g., for accurate mode)
    private val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE) // Use stream mode for real-time processing
        .build()

    // Get a PoseDetector instance
    private val poseDetector: PoseDetector = PoseDetection.getClient(options)

    // Channel to send pose results to
    private val _poseResults = Channel<Pose>(Channel.BUFFERED)
    val poseResults: Flow<Pose> = _poseResults.receiveAsFlow()

    // Atomic flag to prevent processing multiple frames simultaneously
    private val isProcessing = AtomicBoolean(false)

    @SuppressLint("UnsafeOptInUsageExperimentalGetImage")
    override fun analyze(imageProxy: ImageProxy) {
        // If already processing a frame, close the current one and return
        if (isProcessing.getAndSet(true)) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            // Process the image
            poseDetector.process(inputImage)
                .addOnSuccessListener { pose ->
                    // Pose detection successful
                    _poseResults.trySend(pose) // Send the pose result
                    imageProxy.close() // Close the image proxy
                }
                .addOnFailureListener { e ->
                    // Pose detection failed
                    Log.e("MLKitPoseAnalyzer", "Pose detection failed", e)
                    imageProxy.close() // Close the image proxy
                }
                .addOnCompleteListener {
                    // Mark processing as complete
                    isProcessing.set(false)
                }
        } else {
            imageProxy.close() // Close the image proxy if mediaImage is null
            isProcessing.set(false)
        }
    }

    // Remember to close the detector when it's no longer needed
    fun closeDetector() {
        try {
            poseDetector.close()
        } catch (e: IOException) {
            Log.e("MLKitPoseAnalyzer", "Failed to close pose detector", e)
        }
    }
}
