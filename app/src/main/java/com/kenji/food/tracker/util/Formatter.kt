package com.kenji.food.tracker.util

import kotlin.math.floor
import kotlin.math.round

object Formatter {
    /**
     * Formats a [Double] to the [String] representation of an [Int] if possible
     */
    fun formatDecimal(value: Double): String {
        if (floor(value) == value) {
            return value.toInt().toString()
        }

        return (round(value * 100) / 100).toString()
    }
}
