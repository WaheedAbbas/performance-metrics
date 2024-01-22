package com.metrics.performancemetrics.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import com.metrics.performancemetrics.network.APIResponse
import com.metrics.performancemetrics.network.MetricsApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MetricsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var metricsApiHelper: MetricsApiHelper

    private lateinit var viewModel: MetricsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        metricsApiHelper = Mockito.mock(MetricsApiHelper::class.java)
        viewModel = MetricsViewModel(metricsApiHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMetrics success`() = runTest {
        // Arrange
        val mockMetricsList = arrayListOf(createDummyMetric())
        val successResponse = APIResponse(true, 200, "Metrics loaded successfully", mockMetricsList)
        `when`(metricsApiHelper.getAllMetrics()).thenReturn(successResponse)

        // Act
        viewModel.getMetrics()

        // Assert
        val currentState = viewModel.metricsState.value
        assert(currentState?.status == Status.SUCCESS)
        assert(currentState?.data == mockMetricsList)
    }

    @Test
    fun `getMetrics failure`() = runTest {
        // Arrange
        val errorResponse : APIResponse<ArrayList<Metric>> = APIResponse(false, 500, "Internal Server Error", null)
        `when`(metricsApiHelper.getAllMetrics()).thenReturn(errorResponse)

        // Act
        viewModel.getMetrics()

        // Assert
        val currentState = viewModel.metricsState.value
        assert(currentState?.status == Status.ERROR)
        assert(currentState?.data == null)
    }


    @Test
    fun `addNewMetric success`() = runTest {
        // Arrange
        val newMetricBody = NewMetricBody("New Metric")
        val mockMetric = createDummyMetric()
        val apiResponse = APIResponse(true, 200, "Metric added successfully", mockMetric)
        `when`(metricsApiHelper.addNewMetric(newMetricBody)).thenReturn(apiResponse)

        // Act
        viewModel.addNewMetric(newMetricBody)

        // Assert
        val currentState =  viewModel.metricsState.value
        assert(currentState?.status == Status.SUCCESS)
        assert(currentState?.data?.contains(mockMetric) == true)
    }
    @Test
    fun `addNewMetric failure`() = runTest {
        // Arrange
        val newMetricBody = NewMetricBody("New Metric")
        val mockMetric = createDummyMetric()
        viewModel.metricsState.postValue(Resource.success(data = arrayListOf(createDummyMetric())))
        val apiResponse : APIResponse<Metric> = APIResponse(false, 400, "Failed to add new metric", null)
        `when`(metricsApiHelper.addNewMetric(newMetricBody)).thenReturn(apiResponse)

        val initialMetricsList = arrayListOf(createDummyMetric())
        // Act
        viewModel.addNewMetric(newMetricBody)

        // Assert
        val currentState =  viewModel.metricsState.value
        assert(currentState?.status == Status.SUCCESS)
        assert(currentState?.data == initialMetricsList)
    }

    @Test
    fun `addNewMetricValue success`() = runTest{

        // Arrange
        val metricId = 1
        val metricIndex = 0
        val newMetricValueBody = NewMetricValueBody(123.0)
        val mockMetricValue = createDummyMetricValue()
        viewModel.metricsState.postValue(Resource.success(data = arrayListOf(createDummyMetric())))

        val apiResponse = APIResponse(true, 200, "MetricValue added successfully", mockMetricValue)
        `when`(metricsApiHelper.addNewMetricValue(metricId, newMetricValueBody)).thenReturn(apiResponse)

        // Act
        viewModel.addNewMetricValue(metricId, metricIndex, newMetricValueBody)

        // Assert
        val currentState = viewModel.metricsState.value
        assert(currentState?.status == Status.SUCCESS)

        // Check if the new metric value is added to the correct Metric in the ArrayList
        val currentMetricsList = currentState?.data
        val updatedMetric = currentMetricsList?.getOrNull(metricIndex)
        assert(updatedMetric?.metricValues?.contains(mockMetricValue) == true)
    }
    @Test
    fun `addNewMetricValue failure`() = runTest{

        // Arrange
        val metricId = 1
        val metricIndex = 0
        val newMetricValueBody = NewMetricValueBody(123.0)
        val mockMetricValue = createDummyMetricValue()
        viewModel.metricsState.postValue(Resource.success(data = arrayListOf(createDummyMetric())))

        val apiResponse = APIResponse(false, 400, "Failed to add a metric value!", mockMetricValue)
        `when`(metricsApiHelper.addNewMetricValue(metricId, newMetricValueBody)).thenReturn(apiResponse)

        // Act
        val initialMetricValues =  viewModel.metricsState.value?.data?.get(metricIndex)?.metricValues
        viewModel.addNewMetricValue(metricId, metricIndex, newMetricValueBody)

        // Assert
        val currentState = viewModel.metricsState.value
        assert(currentState?.status == Status.SUCCESS)
        assert(viewModel.metricsState.value?.data?.get(metricIndex)?.metricValues == initialMetricValues)

    }

    private fun createDummyMetric(): Metric {
        return Metric(1, "Dummy Metric", Date().toString(), arrayListOf())
    }

    private fun createDummyMetricValue(): MetricValue {
        return MetricValue(1, 42.0, Date().toString(), 1)
    }
}
