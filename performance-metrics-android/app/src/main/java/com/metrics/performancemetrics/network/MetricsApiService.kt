package com.metrics.performancemetrics.network

import com.google.gson.annotations.SerializedName
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MetricsApiService {
    @GET("metrics")
    suspend fun getAllMetrics(): APIResponse<ArrayList<Metric>>

    @POST("metrics")
    suspend fun addNewMetric(@Body newMetricBody: NewMetricBody): APIResponse<Metric>

    @POST("metrics/{metric_id}")
    suspend fun addNewMetricValue(@Path(value = "metric_id", encoded = true) metricID: Int,
                                  @Body newMetricValueBody: NewMetricValueBody): APIResponse<MetricValue>

}
//class MockMetricAPIService() : MetricsApiService
//{
//    var mockMetrics: ArrayList<Metric> = ArrayList()
//    init {
//        val remoteEmployees = Metric(1, "Remote Employees", "time", ArrayList())
//        mockMetrics.add(remoteEmployees)
//    }
//    override suspend fun getAllMetrics(): APIResponse<ArrayList<Metric>> {
//         return APIResponse(true, 200, "Success", mockMetrics)
//    }
//
//    override suspend fun addNewMetric(newMetricBody: NewMetricBody): APIResponse<Metric> {
//        return APIResponse(true, 200, "Success", mockMetrics.get(0))
//    }
//}