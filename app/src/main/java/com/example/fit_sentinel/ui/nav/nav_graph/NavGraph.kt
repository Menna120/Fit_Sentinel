package com.example.fit_sentinel.ui.nav.nav_graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import com.example.fit_sentinel.ui.nav.bottom_nav_bar.BottomNavBar
import com.example.fit_sentinel.ui.nav.nav_graph.form_nav.formNavigator
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.Main
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.mainNavigator

@Composable
fun NavGraph(
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.parent?.equals(navController.graph[Main]) == true

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    onItemClick = { navController.navigate(it.destination) },
                    isSelected = { item ->
                        currentDestination.hierarchy.any { it.hasRoute(item.destination::class) } == true
                    },
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(8.dp)
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            mainNavigator(navController)
            formNavigator(navController)
        }
    }
}