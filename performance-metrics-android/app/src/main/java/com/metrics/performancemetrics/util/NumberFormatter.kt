package com.metrics.performancemetrics.util

import java.math.BigDecimal
import java.text.DecimalFormat

abstract class NumberFormatter {
    abstract fun formatNumber(number: BigDecimal): String
}

class DefaultNumberFormatter : NumberFormatter() {
    private val suffixes = arrayOf(
        "", "Thousand", "Million", "Billion", "Trillion",
        "Quadrillion", "Quintillion", "Sextillion", "Septillion", "Octillion", "Nonillion", "Decillion"
    )
    private val formatter = DecimalFormat("#,###.##")

    override fun formatNumber(number: BigDecimal): String {
        //Values smaller than a billion will be shown as numbers
        if (number < BigDecimal.valueOf(100_0000_000)) {
            return formatter.format(number)
        }

        var magnitude = 0
        var num = number
        while (num >= BigDecimal.valueOf(1000) && magnitude < suffixes.size - 1) {
            num = num.divide(BigDecimal.valueOf(1000))
            magnitude++
        }

        return "${formatter.format(num)} ${suffixes[magnitude]}"
    }
}