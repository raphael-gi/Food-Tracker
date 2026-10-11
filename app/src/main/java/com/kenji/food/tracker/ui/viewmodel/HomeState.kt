package com.kenji.food.tracker.ui.viewmodel

import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.FoodPerDay
import com.kenji.food.tracker.entity.FoodTargetEntity

data class HomeState(
    val caloriesEatenToday: Int? = null,
    val foodPerDays: List<FoodPerDay> = List(7) {
        FoodPerDay(
            day = 0,
            dayOfWeek = it,
            calories = 0,
            protein = 0,
            sugar = 0
        )
    },
    val currentTarget: FoodTargetEntity? = null,
    val selectedDay: List<CountedMealEntity>? = null
)

sealed interface HomeAction {
    data class SelectDay(val day: Long) : HomeAction
    data object CloseOverlay : HomeAction
}
