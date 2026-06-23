package com.kenji.food.tracker.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.entity.RecipeFoodEntity

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(foodEntity: FoodEntity): Long

    @Insert
    suspend fun insertRecipeFood(recipeFood: List<RecipeFoodEntity>)

    @Transaction
    suspend fun insertTest(recipe: Recipe) {
        val createdRecipe = insert(recipe.food)
        val recipeFoods = recipe.foods.map { food ->
            RecipeFoodEntity(
                recipeId = createdRecipe.toInt(),
                foodId = food.id
            )
        }

        insertRecipeFood(recipeFoods)
    }

    @Update
    suspend fun update(foodEntity: FoodEntity)

    @Transaction
    @Query(
        """
        SELECT * FROM food
        ORDER BY food.lastUsed DESC,
        food.name
        """
    )
    fun getAll(): PagingSource<Int, Recipe>

    @Query("SELECT * FROM food WHERE food.isRecipe = false ORDER BY food.lastUsed DESC, food.name")
    fun getAllFoods(): PagingSource<Int, FoodEntity>

    @Transaction
    @Query(
        """
        SELECT * FROM food
        WHERE food.isRecipe = true
        ORDER BY food.lastUsed
        DESC, food.name
        """
    )
    fun getAllRecipes(): PagingSource<Int, Recipe>
}