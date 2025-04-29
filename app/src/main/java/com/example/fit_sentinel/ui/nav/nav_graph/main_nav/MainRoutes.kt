package com.example.fit_sentinel.ui.nav.nav_graph.main_nav

import kotlinx.serialization.Serializable

@Serializable
sealed class MainScreen {
    @Serializable
    object Home : MainScreen()

    @Serializable
    object Reports : MainScreen()

    @Serializable
    object Health : MainScreen()

    @Serializable
    object Settings : MainScreen()

    @Serializable
    data class ExerciseData(val index: Int)
}