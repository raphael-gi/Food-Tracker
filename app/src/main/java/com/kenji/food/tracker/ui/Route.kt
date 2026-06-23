package com.kenji.food.tracker.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    object Home : Route

    @Serializable
    object FoodList : Route

    @Serializable
    object RecipeList : Route

    @Serializable
    object AddFood : Route

    @Serializable
    object AddRecipe : Route

    @Serializable
    object Count : Route

    @Serializable
    object Profile : Route

    @Serializable
    data class FoodTarget(
        val calories: Int,
        val proteins: Int? = null,
        val sugar: Int? = null
    ) : Route
}
