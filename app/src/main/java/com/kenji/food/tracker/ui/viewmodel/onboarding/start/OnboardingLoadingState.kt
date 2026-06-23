package com.kenji.food.tracker.ui.viewmodel.onboarding.start

import com.kenji.food.tracker.entity.FoodTargetEntity

data class OnboardingLoadingState(
    val currentFoodTarget: FoodTargetEntity? = null,
    val isInitialLoading: Boolean = true
) {
    val hasCompletedOnboarding = currentFoodTarget != null
}

sealed interface OnboardingLoadingEffect {
    data object SkipOnboarding : OnboardingLoadingEffect
    data object ContinueOnboarding : OnboardingLoadingEffect
}
