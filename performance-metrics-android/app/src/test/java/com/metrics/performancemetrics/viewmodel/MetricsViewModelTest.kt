package com.metrics.performancemetrics.viewmodel

import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.network.APIResponse
import com.metrics.performancemetrics.network.MetricsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
class MetricsViewModelTest {

    @Mock
    private lateinit var metricsApiService: MetricsApiService

    @Mock
    private lateinit var metricsStateObserver: Observer<Resource<ArrayList<Metric>>>

    private lateinit var metricsViewModel: MetricsViewModel
    var mockMetrics: ArrayList<Metric> = ArrayList()
    private lateinit var server: MockWebServer
    private lateinit var api: MetricsApiService
    @BeforeEach
    fun beforeEach() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("localhost:8080/api/v1"))//Pass any base url like this
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MetricsApiService::class.java)
        val remoteEmployees = Metric(1, "Remote Employees",  "2024-01-22T06:02:02.652279Z", ArrayList())
        val onSiteEmployees = Metric(2, "Onsite Employees",  "2024-01-22T06:03:02.652279Z", ArrayList())
        mockMetrics.add(remoteEmployees)
        mockMetrics.add(onSiteEmployees)
    }

    @Test
    fun `getAllMetrics success`() = runTest {
        val successResponse = APIResponse(true, 200, "Success", mockMetrics)
        val gson: Gson = GsonBuilder().create()
        val json = gson.toJson(successResponse)!!
        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val data = api.getAllMetrics()
        server.takeRequest()

        assertEquals(data, successResponse)
    }

    @AfterEach
    fun afterEach() {
        server.shutdown()
    }
}
