package com.example.fit_sentinel.ui.nav.nav_graph.form_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.formNavigator(navController: NavController) {

    navigation<Form>(startDestination = StartRoute) {
        composable<StartRoute> { }

        composable<FormRoute> { }
    }
}