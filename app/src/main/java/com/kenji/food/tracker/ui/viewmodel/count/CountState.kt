package com.kenji.food.tracker.ui.viewmodel.count

import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.Recipe

data class CountState(
    val isSelectMode: Boolean = false,
    val selectedMeal: Recipe? = null,
    val countedMeal: CountedMealEntity? = null,
    val quantity: Double? = null,
)

sealed interface CountAction {
    data object LaunchCamera : CountAction
    data class CodeScanned(val code: String) : CountAction
    data object ToggleSelectMode : CountAction
    data class SelectMeal(val meal: Recipe) : CountAction
    data class SetMealQuantity(val input: String) : CountAction
    data object CountMeal : CountAction
}

sealed interface CountEffect {
    data object LaunchCamera : CountEffect
    data object ScanNotFound : CountEffect
    data object Finish : CountEffect
}
