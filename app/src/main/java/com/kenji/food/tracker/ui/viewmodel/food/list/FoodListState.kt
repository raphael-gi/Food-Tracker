package com.kenji.food.tracker.ui.viewmodel.food.list

import com.kenji.food.tracker.entity.FoodEntity

data class FoodListState(
    val isLoading: Boolean = false,
    val selectedItems: Set<Int> = emptySet(),
    val query: String = ""
)

sealed interface FoodListAction {
    data object NextPage : FoodListAction
    data class Search(val query: String) : FoodListAction
    data class ToggleSelection(val id: Int) : FoodListAction
    data object DeleteSelected : FoodListAction
    data class SelectItem(val item: FoodEntity) : FoodListAction
}

sealed interface FoodListEffect {
    data class ItemSelected(val item: FoodEntity) : FoodListEffect
}
