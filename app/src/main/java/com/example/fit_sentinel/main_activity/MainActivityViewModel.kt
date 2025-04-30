package com.example.fit_sentinel.main_activity

import androidx.lifecycle.ViewModel
import com.example.fit_sentinel.domain.usecase.shared_preferences.IsOnboardingCompletedUseCase
import com.example.fit_sentinel.nav.nav_graph.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase
) : ViewModel() {
    fun startDestination(): Any {
        val startDestination =
            if (isOnboardingCompletedUseCase.get()) MainScreen.Home else MainScreen.Onboarding
        return startDestination
    }
}