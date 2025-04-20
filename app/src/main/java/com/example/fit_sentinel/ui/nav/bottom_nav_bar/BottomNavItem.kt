package com.example.fit_sentinel.ui.nav.bottom_nav_bar

import androidx.annotation.DrawableRes
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.HealthRoute
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.HomeRoute
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.ReportsRoute
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.SettingsRoute

data class BottomNavItem<T : Any>(
    @DrawableRes val icon: Int,
    val destination: T
)

val navItems = listOf(
    BottomNavItem(R.drawable.home, HomeRoute),
    BottomNavItem(R.drawable.report, ReportsRoute),
    BottomNavItem(R.drawable.health, HealthRoute),
    BottomNavItem(R.drawable.user_setting, SettingsRoute),
)