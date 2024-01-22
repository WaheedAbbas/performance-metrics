package com.metrics.performancemetrics.service

import com.metrics.performancemetrics.network.MetricsApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals

class MetricsApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var metricsApiService: MetricsApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        metricsApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MetricsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getAllMetrics success`() = runBlocking {
        // Arrange
        val successResponse = """{
            "success": true,
            "code": 200,
            "message": "Success",
            "data": [{"id": 1, "name": "Metric 1"}]
        }"""

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(successResponse))

        // Act
        val response = metricsApiService.getAllMetrics()

        // Assert
        assertEquals(true, response.success)
        assertEquals(200, response.code)
        assertEquals("Success", response.message)
        assertEquals(1, response.data?.size)
        assertEquals("Metric 1", response.data?.get(0)?.name)
    }

    @Test
    fun `getAllMetrics error`() = runBlocking {
        // Arrange
        val errorResponse = """{
            "success": false,
            "code": 500,
            "message": "Internal Server Error"
        }"""

        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody(errorResponse))

        // Act
        val response = metricsApiService.getAllMetrics()

        // Assert
        assertEquals(false, response.success)
        assertEquals(500, response.code)
        assertEquals("Internal Server Error", response.message)
        assertEquals(null, response.data)
    }
}
