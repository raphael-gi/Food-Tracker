package com.kenji.food.tracker.ui.viewmodel.profile

import com.kenji.food.tracker.entity.FoodTargetEntity

data class ProfileState(
    val currentFoodTarget: FoodTargetEntity? = null,
    val barcodeScanningEnabled: Boolean
)

sealed interface ProfileAction {
    data object UpdateFoodTarget : ProfileAction
    data object ToggleBarcodePermission : ProfileAction
}

sealed interface ProfileEffect {
    data class OnUpdateFoodTarget(val foodTarget: FoodTargetEntity) : ProfileEffect
}
