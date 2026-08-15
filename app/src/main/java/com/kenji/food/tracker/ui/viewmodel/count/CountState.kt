package com.kenji.food.tracker.ui.viewmodel.count

import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.Recipe
import java.time.Instant

data class CountState(
    val isSelectMealMode: Boolean = false,
    val isSelectDateMode: Boolean = false,
    val selectedMeal: Recipe? = null,
    val countedMeal: CountedMealEntity? = null,
    val quantity: Double? = null,
    val eatenAt: Long = Instant.now().toEpochMilli(),
    val query: String = ""
)

sealed interface CountAction {
    data object LaunchCamera : CountAction
    data class CodeScanned(val code: String) : CountAction
    data object ToggleSelectMealMode : CountAction
    data object ToggleSelectDateMode : CountAction
    data class SelectMeal(val meal: Recipe) : CountAction
    data class SelectDate(val date: Long?) : CountAction
    data class Search(val query: String) : CountAction
    data class SetMealQuantity(val input: String) : CountAction
    data object CountMeal : CountAction
}

sealed interface CountEffect {
    data object LaunchCamera : CountEffect
    data object ScanNotFound : CountEffect
    data object Finish : CountEffect
}
