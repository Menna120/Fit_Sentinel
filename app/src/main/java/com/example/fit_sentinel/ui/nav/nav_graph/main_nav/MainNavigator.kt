package com.example.fit_sentinel.ui.nav.nav_graph.main_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.fit_sentinel.ui.nav.nav_graph.sharedViewModel
import com.example.fit_sentinel.ui.screens.main.health.ExerciseDetailsLayout
import com.example.fit_sentinel.ui.screens.main.health.HealthScreen
import com.example.fit_sentinel.ui.screens.main.health.HealthViewModel
import com.example.fit_sentinel.ui.screens.main.home.HomeScreen
import com.example.fit_sentinel.ui.screens.main.reports.ReportsScreen

fun NavGraphBuilder.mainNavigation(
    navController: NavController,
) {
    composable<MainScreen.Home> { HomeScreen() }

    composable<MainScreen.Reports> { ReportsScreen() }

    composable<MainScreen.Health> {
        val viewModel = it.sharedViewModel<HealthViewModel>(
            navController = navController,
            graphRoute = MainScreen.Health
        )
        HealthScreen(viewModel = viewModel) {
            navController.navigate(MainScreen.ExerciseData(it))
        }
    }

    composable<MainScreen.Settings> { }

    composable<MainScreen.ExerciseData> {
        val exerciseDetails: MainScreen.ExerciseData = it.toRoute()
        val viewModel = it.sharedViewModel<HealthViewModel>(
            navController = navController,
            graphRoute = MainScreen.Health
        )
        val exercises = viewModel.state.value.recommendations

        ExerciseDetailsLayout(
            index = exerciseDetails.index,
            exercisesList = exercises
        ) {
            navController.popBackStack()
        }
    }

}