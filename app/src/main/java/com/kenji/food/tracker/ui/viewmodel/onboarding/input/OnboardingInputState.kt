package com.kenji.food.tracker.ui.viewmodel.onboarding.input

data class OnboardingInputState(
    val calorieTarget: Int? = 2500,
    val proteinTarget: Int? = null,
    val sugarTarget: Int? = null
)

sealed interface OnboardingInputAction {
    data class SetCalorieTarget(val input: String) : OnboardingInputAction
    data class SetProteinTarget(val input: String) : OnboardingInputAction
    data class SetSugarTarget(val input: String) : OnboardingInputAction
    data object Finish : OnboardingInputAction
}

sealed interface OnboardingInputEffect {
    data object FinishOnboardingInput : OnboardingInputEffect
}
