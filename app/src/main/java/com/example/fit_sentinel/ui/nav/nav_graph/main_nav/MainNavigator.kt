package com.example.fit_sentinel.ui.nav.nav_graph.main_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fit_sentinel.ui.screens.main.home.HomeScreen

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    navigation<Main>(startDestination = HomeRoute) {

        composable<HomeRoute> { HomeScreen() }

        composable<ReportsRoute> { }

        composable<HealthRoute> { }

        composable<SettingsRoute> { }
    }
}