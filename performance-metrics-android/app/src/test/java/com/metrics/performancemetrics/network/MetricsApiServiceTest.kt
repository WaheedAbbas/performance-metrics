package com.metrics.performancemetrics.network

import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals
import kotlin.test.fail

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

        try {
            metricsApiService.getAllMetrics()
            fail("Expected HttpException, but call succeeded")
        } catch (e: HttpException) {
            // Verify the HTTP exception details
            assertEquals(500, e.code())
            // Check the error message in the response body
            val errorResponseBody = e.response()?.errorBody()?.string()
            assertEquals(errorResponse, errorResponseBody)
        } catch (e: Exception) {
            fail("Unexpected exception: $e")
        }
    }

    @Test
    fun `addNewMetric success`() = runBlocking {
        // Arrange
        val successResponse = """{
            "success":true,
            "code":201,
            "message":"New Metric Added Successfully!",
            "data":{"id":1,"name":"hybrid employees",
            "created_at":"2024-01-22T15:23:29.484424876Z",
            "metricValues":[]}}"""

        mockWebServer.enqueue(MockResponse().setResponseCode(201).setBody(successResponse))

        // Act
        val response = metricsApiService.addNewMetric(NewMetricBody("hybrid employees"))

        // Assert
        assertEquals(true, response.success)
        assertEquals(201, response.code)
        assertEquals("New Metric Added Successfully!", response.message)
        assertEquals("hybrid employees", response.data!!.name)
    }

    @Test
    fun `addNewMetric error`() {
        runBlocking {
            // Arrange
            val errorResponse = """{
                "success": false,
                "code": 400,
                "message": "Provided arguments are invalid, see data for details.",
                "data": {
                    "name": "Metric name cannot be empty!"
                }
                }"""

            mockWebServer.enqueue(MockResponse().setResponseCode(400).setBody(errorResponse))

            // Act and Assert
            try {
                metricsApiService.addNewMetric(NewMetricBody("NA"))
            } catch (e: HttpException) {
                // Verify the HTTP exception details
                assertEquals(400, e.code())

                // Check the error message in the response body
                val errorResponseBody = e.response()?.errorBody()?.string()
                assertEquals(errorResponse, errorResponseBody)
            } catch (e: Exception) {
                fail("Unexpected exception: $e")
            }
        }
    }

        @Test
        fun `addNewMetricValue success`() = runBlocking {
            // Arrange
            val successResponse = """{
            "success": true,
            "code": 200,
            "message": "Successfully added new metric value!",
            "data": {"id": 1,
            "value": 156.0,
            "created_at": "2024-01-22T15:25:30.150943044Z"
            }
            }"""

            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(successResponse))

            // Act
            val response = metricsApiService.addNewMetricValue(1, NewMetricValueBody(156.0))

            // Assert
            assertEquals(true, response.success)
            assertEquals(200, response.code)
            assertEquals("Successfully added new metric value!", response.message)
        }

        @Test
        fun `addNewMetricValue error`() {
            runBlocking {
            // Arrange
            val errorResponse = """{
            "success": false,
            "code": 404,
            "message": "Metric ID is not valid! 4"
        }"""

            mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(errorResponse))

            // Act and Assert
            try {
                metricsApiService.addNewMetricValue(1, NewMetricValueBody(156.0))
            } catch (e: HttpException) {
                // Verify the HTTP exception details
                assertEquals(404, e.code())

                // Check the error message in the response body
                val errorResponseBody = e.response()?.errorBody()?.string()
                assertEquals(errorResponse, errorResponseBody)
            } catch (e: Exception) {
                fail("Unexpected exception: $e")
            }
        }
    }
}
