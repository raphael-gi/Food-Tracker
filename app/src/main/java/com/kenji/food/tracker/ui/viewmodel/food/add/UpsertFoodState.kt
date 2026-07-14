package com.kenji.food.tracker.ui.viewmodel.food.add

import com.kenji.food.tracker.entity.FoodUnit

data class UpsertFoodState(
    val name: String = "",
    val calories: Int? = null,
    val carbs: Double? = null,
    val proteins: Double? = null,
    val fats: Double? = null,
    val sugar: Double? = null,
    val unit: FoodUnit? = FoodUnit.G,
    val quantity: Double? = 100.0,
    val code: String? = null,
    val isCreate: Boolean,
    val isLoading: Boolean
)

sealed interface UpsertFoodAction {
    data class SetName(val name: String) : UpsertFoodAction
    data class SetCalories(val input: String) : UpsertFoodAction
    data class SetCarbs(val input: String) : UpsertFoodAction
    data class SetProteins(val input: String) : UpsertFoodAction
    data class SetFats(val input: String) : UpsertFoodAction
    data class SetSugar(val input: String) : UpsertFoodAction
    data class SetFoodUnit(val input: FoodUnit) : UpsertFoodAction
    data class SetQuantity(val input: String) : UpsertFoodAction
    data class SetCode(val code: String) : UpsertFoodAction
    data object RemoveCode : UpsertFoodAction
    data object Create : UpsertFoodAction
}

sealed interface UpsertFoodEffect {
    data object NavBack : UpsertFoodEffect
}
