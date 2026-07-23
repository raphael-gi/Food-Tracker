package com.kenji.food.tracker.ui.viewmodel.recipe.add

import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.RecipeFoodEntity

data class UpsertRecipeState(
    val name: String = "",
    val isSelectMode: Boolean = false,
    val portions: Double? = 1.0,
    val selectedFoods: Map<Int, RecipeFoodEntity> = emptyMap(),
    val isCreate: Boolean,
    val isLoading: Boolean
)

sealed interface UpsertRecipeAction {
    data class SetName(val name: String) : UpsertRecipeAction
    data object ToggleSelectMode : UpsertRecipeAction
    data object LaunchCamera : UpsertRecipeAction
    data class CodeScanned(val code: String) : UpsertRecipeAction
    data class ToggleSelection(val food: FoodEntity) : UpsertRecipeAction
    data class SetRecipeFoodQuantity(val food: FoodEntity, val input: String) : UpsertRecipeAction
    data class SetPortions(val input: String) : UpsertRecipeAction
    data object Create : UpsertRecipeAction
}

sealed interface UpsertRecipeEffect {
    data object NavBack : UpsertRecipeEffect
    data object LaunchCamera : UpsertRecipeEffect
    data object ScanNotFound : UpsertRecipeEffect
}
