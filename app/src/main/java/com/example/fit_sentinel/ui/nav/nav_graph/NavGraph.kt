package com.example.fit_sentinel.ui.nav.nav_graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import com.example.fit_sentinel.ui.nav.bottom_nav_bar.BottomNavBar
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.Main
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.mainNavigation
import com.example.fit_sentinel.ui.nav.nav_graph.onboarding_nav.Onboarding
import com.example.fit_sentinel.ui.nav.nav_graph.onboarding_nav.onBoardingNavigation
import com.example.fit_sentinel.ui.screens.on_boarding.OnboardingViewModel
import com.example.fit_sentinel.ui.screens.on_boarding.components.OnboardingProgressBar

@Composable
fun NavGraph(
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar by remember {
        derivedStateOf {
            currentDestination?.parent?.equals(
                navController.graph[Main]
            ) == true
        }
    }

    val showTopBar by remember {
        derivedStateOf {
            currentDestination?.hierarchy?.any {
                it.route == Main::class.qualifiedName
            } == true
        }
    }

    val onboardingViewModel: OnboardingViewModel? = if (showTopBar && navBackStackEntry != null) {
        navBackStackEntry?.sharedViewModel<OnboardingViewModel>(
            navController = navController,
            graphRoute = Onboarding
        )
    } else null


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (showTopBar && onboardingViewModel != null) {
                OnboardingProgressBar(
                    viewModel = onboardingViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    onItemClick = { route ->
                        navController.navigate(route.destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    isSelected = { item ->
                        currentDestination?.hierarchy?.any { it.hasRoute(item.destination::class) } == true
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

        mainNavigation(navController)

            onBoardingNavigation(navController)
        }
    }
}