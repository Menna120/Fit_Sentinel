package com.example.fit_sentinel.ui.screens.forms // Replace with your actual package name

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import com.example.fit_sentinel.ui.theme.Purple40

@Composable
fun PersonalizedProgramScreen(
    currentStep: Int = 4, // Example: current step
    totalSteps: Int = 9, // Example: total steps
    onBackClick: () -> Unit = {}, // Handle back button click
    onContinueClick: (String) -> Unit = {}, // Handle continue button click, pass text input
    onSkipClick: () -> Unit = {} // Handle skip button click
) {
    // State for the text input field
    var chronicDiseaseDescription by remember { mutableStateOf("") }

    Scaffold { paddingValues -> // Use Scaffold for basic Material Design structure
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(horizontal = 16.dp, vertical = 8.dp) // Add screen padding
        ) {
            // --- Top Bar (Back Arrow, Progress Bar, Progress Text) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                // Calculate progress (float between 0.0f and 1.0f)
                val progress =
                    if (totalSteps > 0) currentStep.toFloat() / totalSteps.toFloat() else 0f

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .weight(1f) // Takes available space
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)), // Rounded corners for the bar
                    trackColor = Color.LightGray, // Background color of the progress track
                    color = Purple40  // Color of the progress indicator
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$currentStep / $totalSteps",
                    style = MaterialTheme.typography.headlineSmall // Smaller text
                )
            }

            // --- Title and Subtitle ---
            Text(
                text = "Personalized Program",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Before starting, please answer a few simple questions.",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray, // Lighter color for subtitle
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- Question ---
            Text(
                text = "Do you have any chronic diseases?",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- Text Input Area ---
            OutlinedTextField(
                value = chronicDiseaseDescription,
                onValueChange = { chronicDiseaseDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), // Give it a fixed height or adjust as needed
                label = null, // No floating label
                placeholder = { Text("Describe it here") },
                shape = RoundedCornerShape(8.dp),
//                )
            )

            Spacer(modifier = Modifier.weight(1f)) // Push buttons to the bottom

            // --- Buttons ---
            val Purple200 = null
            MainButton(
                "skip",
                onClick = { onContinueClick(chronicDiseaseDescription) },
                enabled = true,
                showIcon = false
            )
            Spacer(modifier = Modifier.height(10.dp))
            MainButton(
                "Continue",
                onClick = { onContinueClick(chronicDiseaseDescription) },
                enabled = true,
                showIcon = false 
            )


            /*
            Button(
                onClick = { onContinueClick(chronicDiseaseDescription) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // For a gradient, you'd use a background brush or a custom composable.
                // Using a solid color for simplicity here:
                //colors = ButtonDefaults.buttonColors( Purple200), // Light purple
                shape = RoundedCornerShape(28.dp) // Pill shape
            ) {
                Text("Continue", color = Color.White, fontSize = 18.sp)
            }
*/
            Spacer(modifier = Modifier.height(12.dp))
            /*
                        Button(
                            onClick = onSkipClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(Purple40 ), // Dark purple
                            shape = RoundedCornerShape(28.dp) // Pill shape
                        ) {
                            Text("Skip", color = Color.White, fontSize = 18.sp)
                        }
            */
            Spacer(modifier = Modifier.height(16.dp)) // Bottom padding for buttons
        }
    }

}

//-- Preview ---
@Preview(showBackground = true)
@Composable
fun ChronicDiseasePreviewScreen() {
    Fit_SentinelTheme { // Wrap in your app's theme
        PersonalizedProgramScreen()
    }
}
//
