package com.kenji.food.tracker.ui.viewmodel.count

import com.kenji.food.tracker.entity.Recipe

data class CountState(
    val isSelectMode: Boolean = false,
    val selectedMeal: Recipe? = null
)

sealed interface CountAction {
    data object ToggleSelectMode : CountAction
    data class SelectMeal(val meal: Recipe) : CountAction
    object CountMeal: CountAction
}
