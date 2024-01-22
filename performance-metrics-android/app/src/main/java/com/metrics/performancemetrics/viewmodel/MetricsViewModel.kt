package com.metrics.performancemetrics.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.network.MetricsApiService
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import com.metrics.performancemetrics.network.APIResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

class MetricsViewModel(private val metricsApiService: MetricsApiService) : ViewModel() {

    private val _metricsState = MutableLiveData<Resource<ArrayList<Metric>>>(Resource.loading(null))
    val metricsState: MutableLiveData<Resource<ArrayList<Metric>>> = _metricsState
    private val TAG = "MetricsViewModel"
    fun addNewMetric(newMetricBody: NewMetricBody)
    {
        viewModelScope.launch {
            try {
                val addNewMetric: APIResponse<Metric> =
                    this@MetricsViewModel.metricsApiService.addNewMetric(newMetricBody)
                addNewMetric.data?.let {
                    val currentMetricsList : ArrayList<Metric> = _metricsState.value?.data ?: arrayListOf()
                    currentMetricsList.add(it)
                    _metricsState.value = Resource.success(currentMetricsList)
                }
            }catch (e : Exception)
            {
                Log.d(TAG, "addNewMetricValue: Exception ${e.message}")
                //handle exception
                e.printStackTrace()
            }
        }
    }
    fun addNewMetricValue(metricId : Int, metricIndex : Int, newMetricValueBody : NewMetricValueBody)
    {
        viewModelScope.launch {
            try {
                val addNewMetric: APIResponse<MetricValue> =
                    this@MetricsViewModel.metricsApiService.addNewMetricValue(metricId, newMetricValueBody)
                addNewMetric.data?.let {
                    val currentMetricsList : ArrayList<Metric> = _metricsState.value?.data ?: arrayListOf()
                    currentMetricsList.get(metricIndex).metricValues.add(it)
                    _metricsState.value = Resource.success(currentMetricsList)
                }
            }catch (e : Exception)
            {
                Log.d(TAG, "addNewMetricValue: Exception ${e.message}")
                //handle exception
                e.printStackTrace()
            }
        }
    }
    fun getMetrics() {
        viewModelScope.launch {
            try {
                _metricsState.value = Resource.loading(null)

                val response = metricsApiService.getAllMetrics()

                if (response.success) {
                    _metricsState.value = Resource.success(response.data ?: arrayListOf())
                } else {
                    _metricsState.value = Resource.error(null, response.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _metricsState.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }

}
