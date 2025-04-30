package com.example.fit_sentinel.ui.screens.forms
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import com.example.fit_sentinel.ui.theme.Purple40

// Define some placeholder colors based on the image
val Purple200 = Color(0xFFBB86FC) // Example purple, adjust as needed
val Purple500 = Color(0xFF6200EE) // Example darker purple, adjust as needed
val Purple700 = Color(0xFF3700B3) // Example even darker purple, adjust as needed
val Teal200 = Color(0xFF03DAC5) // Example teal
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFE0E0E0) // For border/background elements
val DarkGray = Color(0xFF555555) // For less prominent text

@Composable
fun PersonalizedProgramScreen(
    currentStep: Int = 1,
    totalSteps: Int = 9,
    onBackClick: () -> Unit = {},
    onContinueClick: (name: String) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    val progress = currentStep.toFloat() / totalSteps.toFloat()

    Scaffold(
        // Optional: TopAppBar can be used, but we'll build a custom header for more control
        // topBar = { /* ... */ }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(horizontal = 16.dp), // Add horizontal padding to the screen content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Pushes content apart, button to bottom
        ) {
            // Header Section (Top Row)
            Column(modifier = Modifier.fillMaxWidth()) { // Use a Column to contain header elements
                Spacer(modifier = Modifier.height(16.dp)) // Top padding

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back Button
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .weight(1f) // Takes available space
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)), // Rounded corners for the bar
                        trackColor = Color.LightGray, // Background color of the progress track
                        color = Purple40  // Color of the progress indicator
                    )


                    // Step Indicator
                    Text(
                        text = "$currentStep/$totalSteps",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Space below header

                // Title and Subtitle
                Text(
                    text = "Personalized Program",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Before starting, please answer a few simple questions.",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Question
                Text(
                    text = "What is your name?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Name Input Field
                OutlinedTextField( // OutlinedTextField provides a border
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Andrew") }, // Placeholder text
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(8.dp)) // Add shadow and shape
                        .clip(RoundedCornerShape(8.dp)), // Clip content to the shape
                    colors = TextFieldDefaults.outlinedTextFieldColors( // Customize colors
                        focusedBorderColor = Purple500,
                        unfocusedBorderColor = LightGray,
                        cursorColor = Purple500,
                        backgroundColor = White // Set background color
                        // You can customize label/placeholder colors here too
                    ),
                    shape = RoundedCornerShape(8.dp) // Apply shape to the TextField itself
                )
            }

            // Continue Button (Bottom)
            Column(modifier = Modifier.fillMaxWidth()) { // Use a Column to give padding below the button
                Button(
                    onClick = { onContinueClick(name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Standard button height
                    colors = ButtonDefaults.buttonColors(
                        Purple200), // Button color
                    shape = RoundedCornerShape(8.dp) // Rounded corners
                ) {
                    Text(
                        text = "Continue",
                        color = White, // Text color
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Bottom padding
            }
        }
    }
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier,
    colors: ButtonColors,
    shape: RoundedCornerShape,
    content: @Composable (() -> Unit)
) {
    TODO("Not yet implemented")
}

annotation class buttonColors

private fun TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    cursorColor: Color,
    backgroundColor: Color
): TextFieldColors {
    return TODO("Provide the return value")
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewPersonalizedProgramScreen() {
    PersonalizedProgramScreen()
}
*/
//-- Preview ---
@Preview(showBackground = true)
@Composable
fun PreviewPersonalizedProgramScreen() {
    Fit_SentinelTheme { // Wrap in your app's theme
        PersonalizedProgramScreen()
    }
}
/*
MainButton(
                "Continue",
                onClick = { onContinueClick(chronicDiseaseDescription) },
                enabled = true,
                showIcon = false
            )
 */
