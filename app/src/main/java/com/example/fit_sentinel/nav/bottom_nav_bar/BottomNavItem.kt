package com.example.fit_sentinel.nav.bottom_nav_bar

import androidx.annotation.DrawableRes
import com.example.fit_sentinel.R
import com.example.fit_sentinel.nav.nav_graph.MainScreen

data class BottomNavItem<T : Any>(
    @DrawableRes val icon: Int,
    val destination: T
)

val navItems = listOf(
    BottomNavItem(R.drawable.home, MainScreen.Home),
    BottomNavItem(R.drawable.report, MainScreen.Reports),
    BottomNavItem(R.drawable.health, MainScreen.Health),
    BottomNavItem(R.drawable.user_setting, MainScreen.Settings),
)