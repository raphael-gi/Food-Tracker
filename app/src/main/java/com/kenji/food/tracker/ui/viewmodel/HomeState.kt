package com.kenji.food.tracker.ui.viewmodel

import com.kenji.food.tracker.entity.FoodPerDay
import com.kenji.food.tracker.entity.FoodTargetEntity

data class HomeState(
    val caloriesEatenToday: Int? = null,
    val foodPerDays: List<FoodPerDay> = (0..6).map { FoodPerDay(it, 0, protein = 0, sugar = 0) },
    val currentTarget: FoodTargetEntity? = null
)
