package com.example.fit_sentinel.ui.nav.nav_graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get

@Composable
fun NavGraph(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.parent?.equals(navController.graph[com.example.fit_sentinel.ui.nav.nav_graph.main_nav.Main]) == true

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
//            if (showBottomBar) MainNavigationBar(
//                onItemClick = { navController.navigate(it.destination) },
//                isSelected = { item ->
//                    currentDestination.hierarchy.any { it.hasRoute(item.destination::class) } == true
//                }
//            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {}
    }
}