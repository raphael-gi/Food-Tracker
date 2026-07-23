package com.kenji.food.tracker.util

import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.entity.RecipeFoodEntity
import org.junit.Assert
import org.junit.Test

class CalorieCalculatorTest {
    @Test
    fun `calculate the food`() {
        val food = FoodEntity(
            id = 0,
            name = "",
            calories = 255,
            carbs = 16.2,
            sugar = 5.5,
            protein = 25.0,
            fats = 13.4,

            quantity = 100.0,
            unit = FoodUnit.G,
            isRecipe = false
        )

        val countedMeal = CalorieCalculator.calculateCountedFood(food, 75.0)

        Assert.assertEquals(191, countedMeal.calories)
        Assert.assertEquals(12.15, countedMeal.carbs)
        Assert.assertEquals(4.12, countedMeal.sugar)
        Assert.assertEquals(18.75, countedMeal.protein)
        Assert.assertEquals(10.05, countedMeal.fats)
    }

    @Test
    fun `calculate the recipe`() {
        val recipe = Recipe(
            food = FoodEntity(
                id = 0,
                name = "",
                calories = null,
                quantity = 100.0,
                isRecipe = true,
                unit = FoodUnit.G
            ),
            foods = listOf(
                RecipeFoodEntity(
                    recipeId = 0,
                    foodId = 0,
                    food = FoodEntity(
                        id = 0,
                        name = "",
                        calories = 255,
                        carbs = 16.2,
                        sugar = 5.5,
                        protein = 25.0,
                        fats = 13.4,

                        quantity = 100.0,
                        unit = FoodUnit.G,
                        isRecipe = false
                    ),
                    recipeQuantity = 200.0
                ),
                RecipeFoodEntity(
                    recipeId = 0,
                    foodId = 0,
                    food = FoodEntity(
                        id = 0,
                        name = "",
                        calories = 122,
                        carbs = 25.01,
                        sugar = 5.5,
                        protein = 25.0,
                        fats = 13.4,

                        quantity = 100.0,
                        unit = FoodUnit.G,
                        isRecipe = false
                    ),
                    recipeQuantity = 80.0
                )
            )
        )

        val result = CalorieCalculator.calculateCountedRecipe(recipe, 1.0)

        Assert.assertEquals(608, result.calories)
        Assert.assertEquals(52.41, result.carbs)
        Assert.assertEquals(15.4, result.sugar)
    }

    @Test
    fun `calculate the recipe with different quantity`() {
        val recipe = Recipe(
            food = FoodEntity(
                id = 0,
                name = "",
                calories = null,
                quantity = 100.0,
                isRecipe = true,
                unit = FoodUnit.G
            ),
            foods = listOf(
                RecipeFoodEntity(
                    recipeId = 0,
                    foodId = 0,
                    food = FoodEntity(
                        id = 0,
                        name = "",
                        calories = 255,
                        carbs = 16.2,
                        sugar = 5.5,
                        protein = 25.0,
                        fats = 13.4,

                        quantity = 100.0,
                        unit = FoodUnit.G,
                        isRecipe = false
                    ),
                    recipeQuantity = 200.0
                ),
                RecipeFoodEntity(
                    recipeId = 0,
                    foodId = 0,
                    food = FoodEntity(
                        id = 0,
                        name = "",
                        calories = 122,
                        carbs = 25.01,
                        sugar = 5.5,
                        protein = 25.0,
                        fats = 13.4,

                        quantity = 100.0,
                        unit = FoodUnit.G,
                        isRecipe = false
                    ),
                    recipeQuantity = 80.0
                )
            )
        )

        val result = CalorieCalculator.calculateCountedRecipe(recipe, 1.5)

        Assert.assertEquals(911, result.calories)
        Assert.assertEquals(78.61, result.carbs)
        Assert.assertEquals(23.1, result.sugar)
    }
}