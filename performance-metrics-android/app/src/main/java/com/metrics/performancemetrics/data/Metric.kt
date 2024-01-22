package com.metrics.performancemetrics.data

import com.google.gson.annotations.SerializedName

data class Metric(@SerializedName("id") val id : Int,
                  @SerializedName("name") val name : String,
                  @SerializedName("created_at") val createdAt : String,
                  @SerializedName("metricValues") val metricValues : ArrayList<MetricValue>
    )