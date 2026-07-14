package com.kenji.food.tracker.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class Recipe(
    @Embedded val food: FoodEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RecipeFoodEntity::class,
            parentColumn = "recipeId",
            entityColumn = "foodId"
        ),
    )
    val foods: List<RecipeFoodEntity>
)
