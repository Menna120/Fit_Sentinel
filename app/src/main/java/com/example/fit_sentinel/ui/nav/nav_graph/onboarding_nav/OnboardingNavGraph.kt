package com.example.fit_sentinel.ui.nav.nav_graph.onboarding_nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fit_sentinel.ui.screens.on_boarding.OnboardingViewModel
import com.example.fit_sentinel.ui.screens.on_boarding.components.OnboardingProgressBar

val screensWithTopBar = listOf(
    OnboardingScreen.Name::class.qualifiedName,
    OnboardingScreen.Gender::class.qualifiedName,
    OnboardingScreen.Age::class.qualifiedName
)

@Composable
fun OnboardingNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val onboardingViewModel: OnboardingViewModel = viewModel()

    val showTopBar by remember(navBackStackEntry) {
        derivedStateOf {
            val currentRoute = navBackStackEntry?.destination?.route
            screensWithTopBar.contains(currentRoute)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                OnboardingProgressBar(
                    viewModel = onboardingViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = OnboardingScreen.Introduction,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            onBoardingNavigation(navController)

        }
    }
}