package com.kenji.food.tracker.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.entity.RecipeFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Upsert
    suspend fun upsert(foodEntity: FoodEntity): Long

    @Insert
    suspend fun insertRecipeFood(recipeFood: List<RecipeFoodEntity>)

    @Transaction
    suspend fun upsertRecipe(recipe: Recipe) {
        val createdRecipe = upsert(recipe.food)

        val recipeId = recipe.food.id.takeIf { it > 0 } ?: createdRecipe.toInt()

        deleteRecipeFood(recipeId.toLong())

        val recipeFoods = recipe.foods.map { food ->
            RecipeFoodEntity(
                recipeId = recipeId,
                foodId = food.id
            )
        }

        insertRecipeFood(recipeFoods)
    }

    @Query("DELETE FROM recipe_food WHERE recipeId = :recipeId")
    suspend fun deleteRecipeFood(recipeId: Long)

    @Update
    suspend fun update(foodEntity: FoodEntity)

    @Query("SELECT * FROM food WHERE id = :id")
    fun getFoodById(id: Int): Flow<FoodEntity>

    @Query("SELECT * FROM food WHERE id = :id")
    fun getRecipeById(id: Int): Flow<Recipe>

    @Query("SELECT * FROM food WHERE code = :code")
    suspend fun getFoodByCode(code: String): List<FoodEntity>

    @Transaction
    @Query(
        """
        SELECT * FROM food
        ORDER BY food.lastUsed DESC,
        food.name
        """
    )
    fun getAll(): PagingSource<Int, Recipe>

    @Query(
        """
        SELECT *
        FROM food
        WHERE food.isRecipe = false
        AND food.name LIKE :query
        ORDER BY food.lastUsed DESC, food.name"""
    )
    fun getAllFoods(query: String): PagingSource<Int, FoodEntity>

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

    @Query("DELETE FROM food WHERE food.id IN (:ids)")
    suspend fun delete(ids: Set<Int>)
}