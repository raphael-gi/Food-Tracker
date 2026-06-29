package com.kenji.food.tracker.ui.viewmodel

import com.kenji.food.tracker.entity.CaloriesPerDay
import com.kenji.food.tracker.entity.FoodTargetEntity

data class HomeState(
    val caloriesEatenToday: Int? = null,
    val caloriesPerDay: List<CaloriesPerDay> = (0..6).map { CaloriesPerDay(it, 0) },
    val currentTarget: FoodTargetEntity? = null
)
