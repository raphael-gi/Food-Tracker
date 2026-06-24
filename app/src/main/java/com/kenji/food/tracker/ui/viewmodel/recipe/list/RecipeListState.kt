package com.kenji.food.tracker.ui.viewmodel.recipe.list

data class RecipeListState(
    val isLoading: Boolean = false,
    val selectedItems: Set<Int> = emptySet()
)

sealed interface RecipeListAction {
    data object NextPage : RecipeListAction
    data class ToggleSelection(val id: Int) : RecipeListAction
    data object DeleteSelected : RecipeListAction
}
