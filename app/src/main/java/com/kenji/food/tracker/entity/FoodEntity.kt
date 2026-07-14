package com.kenji.food.tracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val calories: Int?,
    val carbs: Double? = null,
    val sugar: Double? = null,
    val protein: Double? = null,
    val fats: Double? = null,
    val quantity: Double,
    val isRecipe: Boolean,
    val unit: FoodUnit,
    val lastUsed: Long? = null,
    val code: String? = null
)