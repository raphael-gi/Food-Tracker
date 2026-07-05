package com.kenji.food.tracker.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object FoodList : Route

    @Serializable
    data class FoodDetail(val id: Int) : Route

    @Serializable
    data object RecipeList : Route

    @Serializable
    data class RecipeDetail(val id: Int) : Route

    @Serializable
    data class UpsertFood(val id: Int? = null) : Route

    @Serializable
    data class UpsertRecipe(val id: Int? = null) : Route

    @Serializable
    data object Count : Route

    @Serializable
    data object CountHistoryList : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data class FoodTarget(
        val calories: Int,
        val proteins: Int? = null,
        val sugar: Int? = null
    ) : Route

    @Serializable
    data object Scanner : Route
}
