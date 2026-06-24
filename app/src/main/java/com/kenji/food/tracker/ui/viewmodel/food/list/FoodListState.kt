package com.kenji.food.tracker.ui.viewmodel.food.list

data class FoodListState(
    val isLoading: Boolean = false,
    val selectedItems: Set<Int> = emptySet(),
)

sealed interface FoodListAction {
    data object NextPage : FoodListAction
    data class ToggleSelection(val id: Int) : FoodListAction
    data object DeleteSelected : FoodListAction
}
