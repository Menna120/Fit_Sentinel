package com.example.fit_sentinel.ui.nav.nav_graph.main_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.mainNavigator(navController: NavController) {
    navigation<Main>(startDestination = HomeRoute) {
        composable<HomeRoute> { }

        composable<HomeRoute> { }

        composable<HomeRoute> { }

        composable<HomeRoute> { }
    }
}