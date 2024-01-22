package com.metrics.performancemetrics.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.metrics.performancemetrics.network.MetricsApiHelper
import com.metrics.performancemetrics.network.MetricsApiService
import com.metrics.performancemetrics.viewmodel.MetricsViewModel

class MetricsViewModelFactory(private val metricsApiHelper: MetricsApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MetricsViewModel::class.java)) {
            return MetricsViewModel(metricsApiHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
