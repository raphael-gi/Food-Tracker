package com.kenji.food.tracker.ui.viewmodel.recipe.modify

import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.RecipeFoodEntity
import com.kenji.food.tracker.util.ModifyMode

data class ModifyRecipeState(
    val name: String = "",
    val isSelectMode: Boolean = false,
    val portions: Double? = 1.0,
    val selectedFoods: Map<Int, RecipeFoodEntity> = emptyMap(),
    val query: String = "",
    val mode: ModifyMode,
    val isLoading: Boolean = false
)

sealed interface ModifyRecipeAction {
    data class SetName(val name: String) : ModifyRecipeAction
    data object ToggleSelectMode : ModifyRecipeAction
    data object LaunchCamera : ModifyRecipeAction
    data class CodeScanned(val code: String) : ModifyRecipeAction
    data class ToggleSelection(val food: FoodEntity) : ModifyRecipeAction
    data class Search(val query: String) : ModifyRecipeAction
    data class SetRecipeFoodQuantity(val food: FoodEntity, val input: String) : ModifyRecipeAction
    data class SetPortions(val input: String) : ModifyRecipeAction
    data object Create : ModifyRecipeAction
}

sealed interface ModifyRecipeEffect {
    data object NavBack : ModifyRecipeEffect
    data object LaunchCamera : ModifyRecipeEffect
    data object ScanNotFound : ModifyRecipeEffect
}
