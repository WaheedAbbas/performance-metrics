package com.metrics.performancemetrics.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.network.MetricsApiService
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MetricsViewModel(private val metricsApiService: MetricsApiService) : ViewModel() {

    private val _metricsState = MutableLiveData<Resource<ArrayList<Metric>>>(Resource.loading(null))
    val metricsState: MutableLiveData<Resource<ArrayList<Metric>>> = _metricsState

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