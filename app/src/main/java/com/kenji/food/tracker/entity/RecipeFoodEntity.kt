package com.kenji.food.tracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "recipe_food",
    primaryKeys = ["recipeId", "foodId"],
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"]
        ),
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"]
        )
    ]
)
data class RecipeFoodEntity(
    @ColumnInfo(index = true) val recipeId: Int,
    @ColumnInfo(index = true) val foodId: Int
)