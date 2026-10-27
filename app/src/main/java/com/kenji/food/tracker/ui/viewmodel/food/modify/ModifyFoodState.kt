package com.kenji.food.tracker.ui.viewmodel.food.modify

import com.kenji.food.tracker.entity.FoodCategory
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.util.ModifyMode

data class ModifyFoodState(
    val name: String = "",
    val calories: Int? = null,
    val fats: Double? = null,
    val saturatedFats: Double? = null,
    val carbs: Double? = null,
    val sugar: Double? = null,
    val proteins: Double? = null,
    val unit: FoodUnit? = FoodUnit.G,
    val category: FoodCategory? = null,
    val quantity: Double? = 100.0,
    val code: String? = null,
    val mode: ModifyMode,
    val isLoading: Boolean = false
)

sealed interface ModifyFoodAction {
    data class SetName(val name: String) : ModifyFoodAction
    data class SetCalories(val input: String) : ModifyFoodAction
    data class SetFats(val input: String) : ModifyFoodAction
    data class SetSaturatedFats(val input: String) : ModifyFoodAction
    data class SetCarbs(val input: String) : ModifyFoodAction
    data class SetProteins(val input: String) : ModifyFoodAction
    data class SetSugar(val input: String) : ModifyFoodAction
    data class SetFoodUnit(val input: FoodUnit) : ModifyFoodAction
    data class SetFoodCategory(val input: FoodCategory) : ModifyFoodAction
    data class SetQuantity(val input: String) : ModifyFoodAction
    data class SetCode(val code: String) : ModifyFoodAction
    data object RemoveCode : ModifyFoodAction
    data object Create : ModifyFoodAction
}

sealed interface ModifyFoodEffect {
    data object NavBack : ModifyFoodEffect
}
