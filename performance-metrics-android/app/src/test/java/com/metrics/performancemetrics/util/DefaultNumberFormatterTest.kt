package com.metrics.performancemetrics.util

import org.junit.jupiter.api.Assertions.*

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class DefaultNumberFormatterTest {
    @Test
    fun testFormatNumberZero() {
        val formatter = DefaultNumberFormatter()

        val result = formatter.formatNumber(BigDecimal.ZERO)

        assertEquals("0", result)
    }
    @Test
    fun testFormatNumberBelowBillion() {
        val formatter = DefaultNumberFormatter()

        val result = formatter.formatNumber(BigDecimal.valueOf(123456789.12345))

        assertEquals("123,456,789.12", result)
    }

    @Test
    fun testFormatNumberEqualBillion() {
        val formatter = DefaultNumberFormatter()

        val result = formatter.formatNumber(BigDecimal.valueOf(1_000_000_000.12345))

        assertEquals("1 Billion", result)
    }

    @Test
    fun testFormatNumberAboveBillion() {
        val formatter = DefaultNumberFormatter()

        val result = formatter.formatNumber(BigDecimal.valueOf(1_234_567_890_123.45))

        assertEquals("1.23 Trillion", result)
    }

    @Test
    fun testFormatNumberVeryLarge() {
        val formatter = DefaultNumberFormatter()

        val veryLargeNumber = BigDecimal.valueOf(1.23E33) //Scientific notation
        val result = formatter.formatNumber(veryLargeNumber)

        assertEquals("1.23 Decillion", result)
    }
}
