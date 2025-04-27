package com.example.fit_sentinel.ui.screens.main.health.camera

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalGetImage::class)
@Composable
fun ExerciseCounterScreen(
    viewModel: ExerciseCounterViewModel = viewModel() // Get the ViewModel
) {
    val exerciseCount by viewModel.exerciseCount.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            analyzer = viewModel.getFrameAnalyzer() // Pass the analyzer from the ViewModel
        )

        // You can optionally draw the pose landmarks as an overlay on the preview
        // This requires drawing on a Canvas, getting the original image dimensions
        // and the preview view dimensions to scale the landmark coordinates correctly.
        // This is more complex and omitted for brevity here.

        // Exercise Count Display
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Count:",
                fontSize = 24.sp,
                color = Color.White // Adjust color for visibility on camera feed
            )
            Text(
                text = "$exerciseCount",
                fontSize = 48.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color.White // Adjust color for visibility
            )
        }

        // You could add instructions or other UI elements here
    }
}

// Example of how to use the screen in your Activity:
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request camera permission here before setting content
        // For example, using ActivityResultLauncher
        setContent {
            YourAppTheme { // Your app's theme
                ExerciseCounterScreen()
            }
        }
    }
}
*/
