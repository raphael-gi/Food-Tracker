package com.kenji.food.tracker.util

sealed class ModifyMode {
    data class Create(val code: String? = null) : ModifyMode()
    data class Edit(val id: Int) : ModifyMode()
}
