package com.kenji.food.tracker.util

import org.junit.Assert
import org.junit.Test

class FormatterTest {
    @Test
    fun `formats double to int representation correctly`() {
        val formatted = Formatter.formatDecimal(5.0)
        Assert.assertEquals("5", formatted)
    }

    @Test
    fun `formats double correctly`() {
        val formatted = Formatter.formatDecimal(5.2)
        Assert.assertEquals("5.2", formatted)
    }

    @Test
    fun `formats negative double correctly`() {
        val formatted = Formatter.formatDecimal(-5.2)
        Assert.assertEquals("-5.2", formatted)
    }
}