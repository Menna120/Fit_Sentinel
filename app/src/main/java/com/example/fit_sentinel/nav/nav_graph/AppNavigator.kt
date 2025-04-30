package com.example.fit_sentinel.nav.nav_graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.fit_sentinel.ui.screens.main.health.ExerciseDetailsLayout
import com.example.fit_sentinel.ui.screens.main.health.HealthScreen
import com.example.fit_sentinel.ui.screens.main.health.HealthViewModel
import com.example.fit_sentinel.ui.screens.main.home.HomeScreen
import com.example.fit_sentinel.ui.screens.main.reports.ReportsScreen
import com.example.fit_sentinel.ui.screens.main.settings.SettingScreen
import com.example.fit_sentinel.ui.screens.on_boarding.OnboardingScreen

fun NavGraphBuilder.mainNavigation(
    navController: NavController,
) {
    lateinit var viewModel: HealthViewModel

    composable<MainScreen.Onboarding> {
        OnboardingScreen {
            navController.navigate(MainScreen.Home) {
                popUpTo(MainScreen.Onboarding) {
                    inclusive = true
                }
            }
        }
    }

    composable<MainScreen.Home> { HomeScreen() }

    composable<MainScreen.Reports> { ReportsScreen() }

    composable<MainScreen.Health> {
        viewModel = hiltViewModel<HealthViewModel>()

        HealthScreen(viewModel = viewModel) {
            navController.navigate(MainScreen.ExerciseData(it))
        }
    }

    composable<MainScreen.Settings> { SettingScreen() }

    composable<MainScreen.ExerciseData> {
        val exerciseDetails: MainScreen.ExerciseData = it.toRoute()
        val exercises = viewModel.state.value.recommendations

        ExerciseDetailsLayout(
            index = exerciseDetails.index,
            exercisesList = exercises
        ) {
            navController.popBackStack()
        }
    }

}