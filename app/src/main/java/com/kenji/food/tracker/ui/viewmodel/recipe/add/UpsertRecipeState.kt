package com.kenji.food.tracker.ui.viewmodel.recipe.add

import com.kenji.food.tracker.entity.FoodEntity

data class UpsertRecipeState(
    val name: String = "",
    val isSelectMode: Boolean = false,
    val selectedFoods: Map<Int, FoodEntity> = emptyMap(),
    val isCreate: Boolean,
    val isLoading: Boolean
)

sealed interface UpsertRecipeAction {
    data class SetName(val name: String) : UpsertRecipeAction
    data object ToggleSelectMode : UpsertRecipeAction
    data class ToggleSelection(val food: FoodEntity) : UpsertRecipeAction
    data object Create : UpsertRecipeAction
}

sealed interface UpsertRecipeEffect {
    data object NavBack : UpsertRecipeEffect
}
