package com.metrics.performancemetrics.network

import com.metrics.performancemetrics.data.Metric
import retrofit2.http.GET

interface MetricsApiService {
    @GET("metrics")
    suspend fun getAllMetrics(): APIResponse<ArrayList<Metric>>
}
class MockMetricAPIService() : MetricsApiService
{
    var mockMetrics: ArrayList<Metric> = ArrayList()
    init {
        val remoteEmployees = Metric(1, "Remote Employees", "time", ArrayList())
        mockMetrics.add(remoteEmployees)
    }
    override suspend fun getAllMetrics(): APIResponse<ArrayList<Metric>> {
         return APIResponse(true, 200, "Success", mockMetrics)
    }

}