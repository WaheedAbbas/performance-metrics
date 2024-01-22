package com.metrics.performancemetrics.util

import java.time.Instant

object DateFormatter {
    fun formatStringDateToMillis(dateTime : String) : Long
    {
        val parsed = Instant.parse(dateTime)
        return parsed.toEpochMilli()
    }
}