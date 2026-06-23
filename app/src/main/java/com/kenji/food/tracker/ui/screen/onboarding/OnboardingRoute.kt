package com.kenji.food.tracker.ui.screen.onboarding

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface OnboardingRoute : NavKey {
    @Serializable
    data object Loading : OnboardingRoute

    @Serializable
    data object Start : OnboardingRoute

    @Serializable
    data object Input : OnboardingRoute
}
