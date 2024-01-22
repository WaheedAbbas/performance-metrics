package com.metrics.performancemetrics.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.metrics.performancemetrics.average.AverageCalculator
import com.metrics.performancemetrics.R
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.factory.MetricsViewModelFactory
import com.metrics.performancemetrics.network.APIRetrofitClient
import com.metrics.performancemetrics.viewmodel.MetricsViewModel
import com.metrics.performancemetrics.viewmodel.Status

class HomeActivity : AppCompatActivity() {

    private var metricsViewModel: MetricsViewModel? = null
    private var homeSwipeRefreshLayout : SwipeRefreshLayout? = null
    private var metricsListRecyclerAdapter : MetricsListRecyclerAdapter? = null
    private val averageCalculator : AverageCalculator = AverageCalculator()
    private var metricsRecyclerView : RecyclerView? = null
    private var apiErrorMessage : TextView? = null
    private var loadingProgressBar : ProgressBar? = null
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        metricsViewModel = ViewModelProvider(this, MetricsViewModelFactory(APIRetrofitClient.metricsApiService))
            .get(MetricsViewModel::class.java)
        observeMetricsState()
    }
    override fun onStart() {
        super.onStart()
        initViews()
        metricsViewModel?.getMetrics()
    }
    private fun initViews()
    {
        homeSwipeRefreshLayout = findViewById(R.id.home_swipe_refresh_layout)
        metricsRecyclerView = findViewById(R.id.metrics_recycler_view)
        apiErrorMessage = findViewById(R.id.metrics_api_error_txt)
        loadingProgressBar = findViewById(R.id.api_loading_bar)
        metricsListRecyclerAdapter = MetricsListRecyclerAdapter(averageCalculator)
        metricsRecyclerView?.adapter = this.metricsListRecyclerAdapter

        homeSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            this@HomeActivity.metricsViewModel?.getMetrics()
            homeSwipeRefreshLayout?.isRefreshing = false
        })
    }
    private fun observeMetricsState() {
        metricsViewModel?.metricsState?.observe(this, Observer { resource ->

            when (resource.status) {
                Status.SUCCESS -> {
                    handleSuccess(resource.data ?: ArrayList())
                }
                Status.ERROR -> {
                    handleError(resource.message)
                }
                Status.LOADING -> {
                    showLoadingState()
                }
            }
        })
    }

    private fun handleSuccess(metricsList: ArrayList<Metric>) {
        Log.d(TAG, "handleSucess: ${metricsList}")
        metricsListRecyclerAdapter?.setMetricsList(metricsList)

        metricsRecyclerView?.visibility = View.VISIBLE
        loadingProgressBar?.visibility = View.GONE
        apiErrorMessage?.visibility = View.GONE
    }
    private fun handleError(errorMessage: String? = "Something went wrong!") {
        Log.d(TAG, "handleError: ${errorMessage}")
        apiErrorMessage?.text = errorMessage

        apiErrorMessage?.visibility = View.VISIBLE
        metricsRecyclerView?.visibility = View.GONE
        loadingProgressBar?.visibility = View.GONE

    }
    private fun showLoadingState() {
        Log.d(TAG, "Loading...")
        loadingProgressBar?.visibility = View.VISIBLE
        metricsRecyclerView?.visibility = View.GONE
        apiErrorMessage?.visibility = View.GONE
    }
}