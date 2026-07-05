package com.kenji.food.tracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val calories: Int?,
    val carbs: Int? = null,
    val sugar: Int? = null,
    val protein: Int? = null,
    val fats: Int? = null,
    val quantity: Int,
    val isRecipe: Boolean,
    val unit: FoodUnit,
    val lastUsed: Long? = null,
    val code: String? = null
)