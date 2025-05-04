package com.example.fit_sentinel.ui.screens.main.home

sealed class HomeToastEvent {
    data object SessionStarted : HomeToastEvent()
    data object SessionEnded : HomeToastEvent()
}