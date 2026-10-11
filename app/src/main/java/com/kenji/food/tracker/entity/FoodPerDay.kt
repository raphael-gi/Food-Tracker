package com.kenji.food.tracker.entity

data class FoodPerDay(
    val day: Long,
    val dayOfWeek: Int,
    val calories: Int,
    val protein: Int,
    val sugar: Int,
)