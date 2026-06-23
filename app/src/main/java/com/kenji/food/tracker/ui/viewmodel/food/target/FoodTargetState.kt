package com.kenji.food.tracker.ui.viewmodel.food.target

data class FoodTargetState(
    val calories: Int,
    val proteins: Int? = null,
    val sugar: Int? = null,
)

sealed interface FoodTargetAction {
    data class SetCalories(val input: String) : FoodTargetAction
    data class SetProteins(val input: String) : FoodTargetAction
    data class SetSugar(val input: String) : FoodTargetAction
    data object Update : FoodTargetAction
}

sealed interface FoodTargetEffect {
    data object NavBack : FoodTargetEffect
}
