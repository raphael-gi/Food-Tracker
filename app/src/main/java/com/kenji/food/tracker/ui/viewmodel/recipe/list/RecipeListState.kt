package com.kenji.food.tracker.ui.viewmodel.recipe.list

import com.kenji.food.tracker.entity.Recipe

data class RecipeListState(
    val isLoading: Boolean = false,
    val selectedItems: Set<Int> = emptySet()
)

sealed interface RecipeListAction {
    data object NextPage : RecipeListAction
    data class ToggleSelection(val id: Int) : RecipeListAction
    data object DeleteSelected : RecipeListAction
    data class SelectItem(val item: Recipe) : RecipeListAction
}

sealed interface RecipeListEffect {
    data class ItemSelected(val item: Recipe) : RecipeListEffect
}
