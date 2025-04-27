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
    navigation<Onboarding>(startDestination = IntroductionRoute) {

        composable<IntroductionRoute> {

        }

        navigation<Form>(startDestination = NameRoute) {
            composable<NameRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
            composable<GenderRoute> {
            }
            composable<IllnessRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
            composable<ChronicDiseasesRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
            composable<WeightRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
            composable<HeightRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
            composable<AgeRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
            composable<TargetWeightRoute> {
                it.sharedViewModel(navController = navController, graphRoute = Onboarding)
            }
        }
    }
}