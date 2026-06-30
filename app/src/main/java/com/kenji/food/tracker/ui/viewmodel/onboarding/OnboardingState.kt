package com.kenji.food.tracker.ui.viewmodel.onboarding

data class OnboardingState(
    val currentStep: OnboardingStep = OnboardingStep.LOADING,
    val calorieTarget: Int? = 2500,
    val proteinTarget: Int? = null,
    val sugarTarget: Int? = null
)

sealed interface OnboardingAction {
    data object Start : OnboardingAction
    data object StepBack : OnboardingAction
    data object ConfirmCalories : OnboardingAction
    data class SetCalorieTarget(val input: String) : OnboardingAction
    data class SetProteinTarget(val input: String) : OnboardingAction
    data class SetSugarTarget(val input: String) : OnboardingAction
    data object Finish : OnboardingAction
}

sealed interface OnboardingEffect {
    data object FinishOnboarding : OnboardingEffect
}

enum class OnboardingStep {
    LOADING,
    START,
    CALORIE_INPUT,
    ADDITIONAL_INPUT
}
