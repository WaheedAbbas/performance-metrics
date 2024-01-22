package com.metrics.performancemetrics.data

import com.google.gson.annotations.SerializedName


data class NewMetricBody(
    @SerializedName("name")
    val name: String
)
