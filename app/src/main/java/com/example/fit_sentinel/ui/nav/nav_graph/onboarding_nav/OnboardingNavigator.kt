package com.example.fit_sentinel.ui.nav.nav_graph.onboarding_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fit_sentinel.ui.nav.nav_graph.sharedViewModel
import com.example.fit_sentinel.ui.screens.on_boarding.OnboardingViewModel

fun NavGraphBuilder.onBoardingNavigation(
    navController: NavController
) {

    composable<OnboardingScreen.Introduction> {

    }

    navigation<Form>(startDestination = OnboardingScreen.Name) {
        composable<OnboardingScreen.Name> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
        composable<OnboardingScreen.Gender> {
        }
        composable<OnboardingScreen.Illness> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
        composable<OnboardingScreen.ChronicDiseases> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
        composable<OnboardingScreen.Weight> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
        composable<OnboardingScreen.Height> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
        composable<OnboardingScreen.Age> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
        composable<OnboardingScreen.TargetWeight> {
            it.sharedViewModel<OnboardingViewModel>(
                navController = navController,
                graphRoute = OnboardingScreen
            )
        }
    }
}