package com.metrics.performancemetrics.data

import com.google.gson.annotations.SerializedName

data class NewMetricValueBody(
    @SerializedName("value")
    val value: Double
)
