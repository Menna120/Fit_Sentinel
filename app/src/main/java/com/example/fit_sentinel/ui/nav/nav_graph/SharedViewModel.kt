package com.example.fit_sentinel.ui.nav.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    graphRoute: Any
): T {
    val navGraph =
        remember(navController.graph) {
            navController.graph.find { destination -> destination.route == graphRoute::class.qualifiedName } as? NavGraph
        }

    val graphEntry =
        remember(this, navGraph) { navGraph?.let { navController.getBackStackEntry(it.id) } }

    return if (graphEntry != null) hiltViewModel(viewModelStoreOwner = graphEntry) else {
        hiltViewModel(viewModelStoreOwner = this)
    }
}