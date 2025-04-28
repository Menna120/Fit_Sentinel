package com.example.fit_sentinel.ui.nav.nav_graph.exercise_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.HealthRoute
import com.example.fit_sentinel.ui.nav.nav_graph.sharedViewModel
import com.example.fit_sentinel.ui.screens.main.health.ExerciseDetailsLayout
import com.example.fit_sentinel.ui.screens.main.health.HealthViewModel

fun NavGraphBuilder.exerciseNavigation(
    navController: NavController,
) {
    composable<ExerciseDetails> {
        val exerciseDetails: ExerciseDetails = it.toRoute()
        val viewModel = it.sharedViewModel<HealthViewModel>(
            navController = navController,
            graphRoute = HealthRoute
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