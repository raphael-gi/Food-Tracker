package com.kenji.food.tracker.ui.viewmodel.count

import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.Recipe

data class CountState(
    val isSelectMode: Boolean = false,
    val selectedMeal: Recipe? = null,
    val countedMeal: CountedMealEntity? = null,
    val quantity: Int? = null,
)

sealed interface CountAction {
    data object ToggleSelectMode : CountAction
    data class SelectMeal(val meal: Recipe) : CountAction
    data class SetMealQuantity(val input: String) : CountAction
    data object CountMeal : CountAction
}

sealed interface CountEffect {
    data object Finish : CountEffect
}
