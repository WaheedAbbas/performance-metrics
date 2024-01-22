package com.metrics.performancemetrics.network

import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
class MetricsApiHelperTest {

    @Mock
    private lateinit var metricsApiService: MetricsApiService

    private lateinit var metricsApiHelper: MetricsApiHelper

    @Before
    fun setUp() {
        metricsApiService = Mockito.mock(MetricsApiService::class.java)
        metricsApiHelper = MetricsApiHelper(metricsApiService)
    }

    @Test
    fun `getAllMetrics success`() = runBlocking {
        // Arrange
        val mockResponse = APIResponse<ArrayList<Metric>>(true, 200, "Success", ArrayList())
        `when`(metricsApiService.getAllMetrics()).thenReturn(mockResponse)

        // Act
        val result = metricsApiHelper.getAllMetrics()

        // Assert
        assertEquals(mockResponse, result)
    }

    @Test
    fun `getAllMetrics error`() = runBlocking {
        // Arrange
        val responseJson = """{
            "success": false,
            "code": 500,
            "message": "Internal Server Error"
        }"""
        val errorResponse = Response.error<ArrayList<Any>>(
            500,
            responseJson.toResponseBody("application/json".toMediaTypeOrNull())
        )
        val httpException = HttpException(errorResponse)
        `when`(metricsApiService.getAllMetrics()).thenThrow(httpException)

        // Act
        val result = metricsApiHelper.getAllMetrics()

        // Assert
        assertEquals(false, result.success)
        assertEquals(500, result.code)
        assertEquals("Internal Server Error", result.message)
        assertEquals(null, result.data)
    }
    @Test
    fun `addNewMetric success`() = runBlocking {
        // Arrange
        val newMetricBody = NewMetricBody("New Metric Name")
        val mockResponse = APIResponse(true, 200, "Metric added successfully", Metric(1, "New Metric Name", "2024-01-22T15:23:29.484425Z", arrayListOf()))
        `when`(metricsApiService.addNewMetric(newMetricBody)).thenReturn(mockResponse)

        // Act
        val result = metricsApiHelper.addNewMetric(newMetricBody)

        // Assert
        assertEquals(mockResponse, result)
    }

    @Test
    fun `addNewMetric error`() = runBlocking {
        // Arrange
        val newMetricBody = NewMetricBody("New Metric Name")
        val jsonResponse =  """{
                "success": false,
                "code": 400,
                "message": "Provided arguments are invalid, see data for details.",
                "data": {
                    "name": "Metric name cannot be empty!"
                }
                }"""
        val errorResponse = Response.error<Metric>(
            500,
            jsonResponse.toResponseBody("application/json".toMediaTypeOrNull())
        )
        val httpException = HttpException(errorResponse)
        `when`(metricsApiService.addNewMetric(newMetricBody)).thenThrow(httpException)

        // Act
        val result = metricsApiHelper.addNewMetric(newMetricBody)

        // Assert
        assertEquals(false, result.success)
        assertEquals(500, result.code)
        assertEquals("Provided arguments are invalid, see data for details.", result.message)
        assertEquals(null, result.data)
    }

    @Test
    fun `addNewMetricValue success`() = runBlocking {
        // Arrange
        val metricId = 1
        val newMetricValueBody = NewMetricValueBody(156.0)
        val mockResponse = APIResponse(true, 200, "MetricValue added successfully", MetricValue(1, 156.0, "2024-01-22T15:25:30.150943Z", 1))
        `when`(metricsApiService.addNewMetricValue(metricId, newMetricValueBody)).thenReturn(mockResponse)

        // Act
        val result = metricsApiHelper.addNewMetricValue(metricId, newMetricValueBody)

        // Assert
        assertEquals(mockResponse, result)
    }

    @Test
    fun `addNewMetricValue error`() = runBlocking {
        // Arrange
        val metricId = 1
        val newMetricValueBody = NewMetricValueBody(156.0)
        val jsonResponse = """{
            "success": false,
            "code": 404,
            "message": "Metric ID is not valid! 4"
        }"""
        val errorResponse = Response.error<MetricValue>(
            500,
            jsonResponse.toResponseBody("application/json".toMediaTypeOrNull())
        )
        val httpException = HttpException(errorResponse)
        `when`(metricsApiService.addNewMetricValue(metricId, newMetricValueBody)).thenThrow(httpException)

        // Act
        val result = metricsApiHelper.addNewMetricValue(metricId, newMetricValueBody)

        // Assert
        assertEquals(false, result.success)
        assertEquals(500, result.code)
        assertEquals("Metric ID is not valid! 4", result.message)
        assertEquals(null, result.data)
    }


}