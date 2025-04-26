package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.Main
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.AgeRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.ChronicDiseasesRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.Form
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.GenderRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.HeightRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.IllnessRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.IntroductionRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.NameRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.OnBoarding
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.TargetWeightRoute
import com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav.WeightRoute

@OptIn(ExperimentalMaterial3Api::class) // Scaffold is still experimental
@Composable
fun OnboardingFlowContainer(
    // NavController passed from the outer NavHost (e.g., in your MainActivity)
    navController: NavController,
    startDestination: Any,
    viewModel: OnboardingViewModel = viewModel() // ViewModel scoped to this graph or Activity
) {

}

// Modify your NavGraphBuilder extension function to accept ViewModel
// and call the content composables
fun NavGraphBuilder.onBoardingDestinations(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    // Define the Introduction screen
    composable<IntroductionRoute> {
        IntroductionScreenContent(navController = navController, viewModel = viewModel)
    }

    // Define the nested Form graph
    navigation<Form>(startDestination = NameRoute) {
        composable<NameRoute> {
//            NameScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<GenderRoute> {
            GenderScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<IllnessRoute> {
//            IllnessScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<ChronicDiseasesRoute> {
//            ChronicDiseasesScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<WeightRoute> {
//            WeightScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<HeightRoute> {
//            HeightScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<AgeRoute> {
//            AgeScreenContent(navController = navController, viewModel = viewModel)
        }
        composable<TargetWeightRoute> {
//            TargetWeightScreenContent(navController = navController, viewModel = viewModel)
        }
        // Add a destination for after the form is complete if needed within the onboarding flow
        // For example, a summary screen before the final 'Completed' screen.
    }

    // Add a destination for the final "Completed" screen after onboarding
    // This might not need the progress bar anymore, so it could be outside the Scaffold content
    // if navigated to using navController.navigate(MainAppRoute) with popUpTo.
    // If you want the progress bar to show 9/9, keep it within the NavHost/Scaffold here.
//    composable<OnBoardingCompletedRoute> { // Define a route for completion
//        OnboardingCompletedScreenContent(navController = navController, viewModel = viewModel)
//    }
}

// Example: Content for the completed screen (might still show 9/9 progress)
@Composable
fun OnboardingCompletedScreenContent(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Onboarding Completed!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            // Navigate to the main part of the app and clear the onboarding stack
            navController.navigate(Main) {
                popUpTo(OnBoarding) { inclusive = true } // Pop all onboarding screens
            }
            // Optional: Reset ViewModel state if a new onboarding might occur later
            viewModel.resetProgress()
        }) {
            Text("Go to App")
        }
    }
}