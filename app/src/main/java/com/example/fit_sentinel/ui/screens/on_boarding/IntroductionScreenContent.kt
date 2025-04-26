package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.IllnessRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.NameRoute

// Example: Screen content for the Introduction step
@Composable
fun IntroductionScreenContent(
    navController: NavController,
    viewModel: OnboardingViewModel // Can access ViewModel here if needed
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Use padding from the outer Scaffold
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the Onboarding!")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            // Navigate to the first screen of the form graph
            navController.navigate(NameRoute)
        }) {
            Text("Start")
        }
    }
}

// Example: Screen content for the Gender step (similar to your image)
@Composable
fun GenderScreenContent(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val currentStep by viewModel.currentStep.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Use padding from the outer Scaffold
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Personalized Program", style = MaterialTheme.typography.headlineMedium)
        Text("Before starting, please answer a few simple questions.")
        Spacer(modifier = Modifier.height(16.dp))
        Text("What is your gender?", style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Your Male/Female selection UI (e.g., using Card or Button)
            Button(onClick = { /* Handle Male selection */ }) { Text("Male") }
            Button(onClick = { /* Handle Female selection */ }) { Text("Female") }
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes button to the bottom

        Button(
            onClick = {
                // Update progress *before* navigating if this step contributes to it
                // In your image, it's 2/9, so selecting gender IS a step.
                viewModel.nextStep()
                // Navigate to the next screen in the form flow
                navController.navigate(IllnessRoute)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
        // Add a back button if your flow allows going back
        if (currentStep > 1) { // Only show back button if not on the first step
            Button(
                onClick = {
                    viewModel.previousStep()
                    navController.popBackStack() // Navigate back using navigation controller
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}