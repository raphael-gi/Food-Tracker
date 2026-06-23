package com.kenji.food.tracker.ui.viewmodel.recipe.add

import com.kenji.food.tracker.entity.FoodEntity

data class AddRecipeState(
    val name: String = "",
    val isSelectMode: Boolean = false,
    val selectedFoods: Map<Int, FoodEntity> = emptyMap(),
)

sealed interface AddRecipeAction {
    data class SetName(val name: String) : AddRecipeAction
    data object ToggleSelectMode : AddRecipeAction
    data class ToggleSelection(val food: FoodEntity) : AddRecipeAction
    data object Create : AddRecipeAction
}

sealed interface AddRecipeEffect {
    data object NavBack : AddRecipeEffect
}
