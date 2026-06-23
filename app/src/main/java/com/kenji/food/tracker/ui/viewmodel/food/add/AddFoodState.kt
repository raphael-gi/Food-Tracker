package com.kenji.food.tracker.ui.viewmodel.food.add

import com.kenji.food.tracker.entity.FoodUnit

data class AddFoodState(
    val name: String = "",
    val calories: Int? = null,
    val carbs: Int? = null,
    val proteins: Int? = null,
    val fats: Int? = null,
    val unit: FoodUnit? = FoodUnit.G,
    val quantity: Int? = 100,
)

sealed interface AddFoodAction {
    data class SetName(val name: String) : AddFoodAction
    data class SetCalories(val input: String) : AddFoodAction
    data class SetCarbs(val input: String) : AddFoodAction
    data class SetProteins(val input: String) : AddFoodAction
    data class SetFats(val input: String) : AddFoodAction
    data class SetFoodUnit(val input: FoodUnit) : AddFoodAction
    data class SetQuantity(val input: String) : AddFoodAction
    data object Create : AddFoodAction
}

sealed interface AddFoodEffect {
    data object NavBack : AddFoodEffect
}
