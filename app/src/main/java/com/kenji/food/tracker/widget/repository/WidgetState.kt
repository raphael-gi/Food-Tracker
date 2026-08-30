package com.kenji.food.tracker.widget.repository

import com.kenji.food.tracker.entity.FoodTargetEntity

data class WidgetState(
    val target: FoodTargetEntity,
    val caloriesToday: Int,
    val proteinsToday: Double
) : WidgetUIState

sealed interface WidgetUIState {
    data object NoTarget : WidgetUIState
    data object Loading : WidgetUIState
}

