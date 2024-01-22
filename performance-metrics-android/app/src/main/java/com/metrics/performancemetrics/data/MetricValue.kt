package com.metrics.performancemetrics.data

import com.google.gson.annotations.SerializedName
import com.metrics.performancemetrics.util.DateFormatter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class MetricValue(@SerializedName("id") val id : Int,
                       @SerializedName("value") val value : Double,
                       @SerializedName("created_at") val createdAt : String,
                       @SerializedName("metric_id") val metricId : Int)
fun MetricValue.createdAtMillis() : Long
{
    return DateFormatter.formatStringDateToMillis(createdAt)
}