package com.kenji.food.tracker.util

import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.Recipe
import kotlin.math.round
import kotlin.math.roundToInt

object CalorieCalculator {
    private fun getFoodProperty(baseValue: Double?, multiplier: Double): Double? {
        if (baseValue == null) {
            return null
        }

        val calculatedValue = baseValue.times(multiplier)

        return round(calculatedValue * 100) / 100
    }

    /**
     * Calculate the amount of food actually eaten based on a food and a quantity
     *
     * @param food The food
     * @param quantity The quantity used to multiply the properties of the food by
     * @return A [CountedMealEntity] with the calculated values
     */
    fun calculateCountedFood(food: FoodEntity, quantity: Double): CountedMealEntity {
        val multiplier = quantity / food.quantity

        return CountedMealEntity(
            id = 0,
            name = food.name,
            calories = (food.calories?.times(multiplier))?.roundToInt(),
            carbs = getFoodProperty(food.carbs, multiplier),
            sugar = getFoodProperty(food.sugar, multiplier),
            protein = getFoodProperty(food.protein, multiplier),
            fats = getFoodProperty(food.fats, multiplier),
        )
    }

    fun calculateCountedRecipe(recipe: Recipe): CountedMealEntity {
        val initialValue = CountedMealEntity(
            id = 0,
            name = recipe.food.name,
            calories = 0,
            carbs = 0.0,
            sugar = 0.0,
            protein = 0.0,
            fats = 0.0,
        )

        return recipe.foods.fold(initialValue) { acc, entity ->
            val multiplier = entity.recipeQuantity / entity.food.quantity

            return@fold acc.copy(
                calories = acc.calories?.plus(
                    (entity.food.calories ?: 0).times(multiplier).roundToInt()
                ),
                carbs = acc.carbs?.plus(getFoodProperty(entity.food.carbs, multiplier) ?: 0.0),
                sugar = acc.sugar?.plus(getFoodProperty(entity.food.sugar, multiplier) ?: 0.0),
                protein = acc.protein?.plus(
                    getFoodProperty(entity.food.protein, multiplier) ?: 0.0
                ),
                fats = acc.fats?.plus(getFoodProperty(entity.food.fats, multiplier) ?: 0.0),
            )
        }
    }
}