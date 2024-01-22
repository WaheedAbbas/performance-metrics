package com.metrics.performancemetrics.data

import com.google.gson.annotations.SerializedName
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
   // val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX")
    val parsed = Instant.parse(createdAt)
  //  val localDateTime = LocalDateTime.parse(createdAt, formatter)
  //  val instant = localDateTime.atZone(ZoneOffset.UTC).toInstant()
    // Extract milliseconds from Instant
    return parsed.toEpochMilli()
}