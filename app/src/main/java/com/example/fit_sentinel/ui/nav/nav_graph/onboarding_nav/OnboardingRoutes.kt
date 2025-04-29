package com.example.fit_sentinel.ui.nav.nav_graph.onboarding_nav

import kotlinx.serialization.Serializable

@Serializable
sealed class OnboardingScreen {
    @Serializable
    object Introduction : OnboardingScreen()

    @Serializable
    object Form : OnboardingScreen()

    @Serializable
    object Name : OnboardingScreen()

    @Serializable
    object Gender : OnboardingScreen()

    @Serializable
    object Illness : OnboardingScreen()

    @Serializable
    object ChronicDiseases : OnboardingScreen()

    @Serializable
    object Weight : OnboardingScreen()

    @Serializable
    object Height : OnboardingScreen()

    @Serializable
    object Age : OnboardingScreen()

    @Serializable
    object TargetWeight : OnboardingScreen()
}

@Serializable
object Form