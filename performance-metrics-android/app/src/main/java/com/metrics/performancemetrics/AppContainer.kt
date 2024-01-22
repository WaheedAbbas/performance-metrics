package com.metrics.performancemetrics

import com.metrics.performancemetrics.average.AverageCalculator
import com.metrics.performancemetrics.network.APIRetrofitClient
import com.metrics.performancemetrics.network.MetricsApiHelper

class AppContainer {

    val metricsApiHelper : MetricsApiHelper = MetricsApiHelper(APIRetrofitClient.metricsApiService)
    val averageCalculator : AverageCalculator = AverageCalculator()

}