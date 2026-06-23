package com.kenji.food.tracker.ui.viewmodel.profile

import com.kenji.food.tracker.entity.FoodTargetEntity

data class ProfileState(
    val currentFoodTarget: FoodTargetEntity? = null
)

sealed interface ProfileAction {
    data object UpdateFoodTarget : ProfileAction
}

sealed interface ProfileEffect {
    data class OnUpdateFoodTarget(val foodTarget: FoodTargetEntity) : ProfileEffect
}
