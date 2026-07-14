package com.kenji.food.tracker.util

import kotlin.math.floor

object Formatter {
    /**
     * Formats a [Double] to the [String] representation of an [Int] if possible
     */
    fun formatDecimal(value: Double): String {
        if (floor(value) == value) {
            return value.toInt().toString()
        }

        return value.toString()
    }
}
