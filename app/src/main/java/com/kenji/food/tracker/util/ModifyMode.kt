package com.kenji.food.tracker.util

sealed class ModifyMode {
    data object Create : ModifyMode()
    data class Edit(val id: Int) : ModifyMode()
}
