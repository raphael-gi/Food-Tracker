package com.kenji.food.tracker.ui.viewmodel.count.history

data class CountHistoryState(
    val selectedItems: Set<Int> = emptySet(),
)

sealed interface CountHistoryAction {
    data class ToggleSelection(val id: Int) : CountHistoryAction
    data object DeleteSelected : CountHistoryAction
}
