package com.example.fit_sentinel.ui.nav.nav_graph.on_boarding_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fit_sentinel.ui.nav.nav_graph.sharedViewModel
import com.example.fit_sentinel.ui.screens.on_boarding.OnboardingViewModel

fun NavGraphBuilder.onBoardingNavigation(
    navController: NavController
) {
    navigation<OnBoarding>(startDestination = IntroductionRoute) {

        composable<IntroductionRoute> {

        }

        navigation<Form>(startDestination = NameRoute) {
            composable<NameRoute> {
                it.sharedViewModel<OnboardingViewModel>(navController)
            }
            composable<GenderRoute> { }
            composable<IllnessRoute> { }
            composable<ChronicDiseasesRoute> { }
            composable<WeightRoute> { }
            composable<HeightRoute> { }
            composable<AgeRoute> { }
            composable<TargetWeightRoute> { }
        }
    }
}