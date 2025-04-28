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
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
            composable<GenderRoute> {
            }
            composable<IllnessRoute> {
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
            composable<ChronicDiseasesRoute> {
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
            composable<WeightRoute> {
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
            composable<HeightRoute> {
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
            composable<AgeRoute> {
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
            composable<TargetWeightRoute> {
                it.sharedViewModel<OnboardingViewModel>(
                    navController = navController,
                    graphRoute = Onboarding
                )
            }
        }
    }
}