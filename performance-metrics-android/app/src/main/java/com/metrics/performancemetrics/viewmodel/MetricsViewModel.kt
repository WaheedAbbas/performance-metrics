package com.metrics.performancemetrics.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import com.metrics.performancemetrics.network.APIResponse
import com.metrics.performancemetrics.network.MetricsApiHelper
import com.metrics.performancemetrics.network.MetricsApiService
import kotlinx.coroutines.launch

class MetricsViewModel(private val metricsApiHelper : MetricsApiHelper) : ViewModel() {

    private val _metricsState = MutableLiveData<Resource<ArrayList<Metric>>>(Resource.loading(null))
    val metricsState: MutableLiveData<Resource<ArrayList<Metric>>> = _metricsState
    fun addNewMetric(newMetricBody: NewMetricBody)
    {
        viewModelScope.launch {
            val addNewMetric: APIResponse<Metric> = metricsApiHelper.addNewMetric(newMetricBody)
            addNewMetric.data?.let {
                val currentMetricsList : ArrayList<Metric> = _metricsState.value?.data ?: arrayListOf()
                currentMetricsList.add(it)
                _metricsState.value = Resource.success(currentMetricsList)
            }
        }
    }
    fun addNewMetricValue(metricId : Int, metricIndex : Int, newMetricValueBody : NewMetricValueBody)
    {
        viewModelScope.launch {
            val addNewMetric: APIResponse<MetricValue> = metricsApiHelper.addNewMetricValue(metricId, newMetricValueBody)
            addNewMetric.data?.let {
                val currentMetricsList : ArrayList<Metric> = _metricsState.value?.data ?: arrayListOf()
                currentMetricsList[metricIndex].metricValues.add(it)
                _metricsState.value = Resource.success(currentMetricsList)
            }
        }
    }
    fun getMetrics() {
        _metricsState.value = Resource.loading(null)
        viewModelScope.launch {
            val response = metricsApiHelper.getAllMetrics()
            if (response.success) {
                _metricsState.value = Resource.success(response.data ?: arrayListOf())
            } else {
                _metricsState.value = Resource.error(null, response.message ?: "Unknown error")
            }
        }
    }
}
