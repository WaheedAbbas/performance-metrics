package com.metrics.performancemetrics.data

import com.google.gson.annotations.SerializedName
import com.metrics.performancemetrics.util.DateFormatter

data class Metric(@SerializedName("id") val id : Int,
                  @SerializedName("name") val name : String,
                  @SerializedName("created_at") val createdAt : String,
                  @SerializedName("metricValues") val metricValues : ArrayList<MetricValue>
    )
fun Metric.createdAtMillis() : Long
{
    return DateFormatter.formatStringDateToMillis(createdAt)
}