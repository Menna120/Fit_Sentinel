package com.example.fit_sentinel.nav.nav_graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fit_sentinel.nav.bottom_nav_bar.BottomNavBar
import com.example.fit_sentinel.nav.bottom_nav_bar.navItems

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavGraph(
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar by remember(navBackStackEntry?.destination) {
        derivedStateOf {
            val currentRoute = navBackStackEntry?.destination?.route
            navItems.any { it.destination::class.qualifiedName == currentRoute }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                        navBackStackEntry?.destination?.route == item.destination::class.qualifiedName
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(8.dp)
                )
            }
        },
        containerColor = Color.Transparent,
        contentColor = Color.Transparent
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            mainNavigation(navController)
        }
    }
}