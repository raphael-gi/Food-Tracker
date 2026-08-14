package com.kenji.food.tracker.ui.viewmodel.count.history

data class CountHistoryState(
    val selectedItems: Set<Int> = emptySet(),
    val query: String = ""
)

sealed interface CountHistoryAction {
    data class ToggleSelection(val id: Int) : CountHistoryAction
    data object DeleteSelected : CountHistoryAction
    data class Search(val query: String) : CountHistoryAction
}
