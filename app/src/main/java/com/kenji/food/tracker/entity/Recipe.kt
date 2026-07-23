package com.kenji.food.tracker.entity

import androidx.room.Embedded
import androidx.room.Relation

data class Recipe(
    @Embedded val food: FoodEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId",
    )
    val foods: List<RecipeFoodEntity>
)
