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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.metrics.performancemetrics.AppContainer
import com.metrics.performancemetrics.MyApplication
import com.metrics.performancemetrics.average.AverageCalculator
import com.metrics.performancemetrics.R
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import com.metrics.performancemetrics.factory.MetricsViewModelFactory
import com.metrics.performancemetrics.network.APIRetrofitClient
import com.metrics.performancemetrics.viewmodel.MetricsViewModel
import com.metrics.performancemetrics.viewmodel.Status

class HomeActivity : AppCompatActivity() {

    private var metricsViewModel: MetricsViewModel? = null
    private var homeSwipeRefreshLayout : SwipeRefreshLayout? = null
    private var addNewMetricFAB : FloatingActionButton? = null
    private var metricsListRecyclerAdapter : MetricsListRecyclerAdapter? = null
    private var metricsRecyclerView : RecyclerView? = null
    private var apiErrorMessage : TextView? = null
    private var loadingProgressBar : ProgressBar? = null
    private var newMetricInputDialog : NewMetricInputDialog? = null
    private var newMetricValueDialog : NewMetricValueInputDialog? = null
    private lateinit var appContainer : AppContainer
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        appContainer = (application as MyApplication).appContainer
        metricsViewModel = ViewModelProvider(this, MetricsViewModelFactory(appContainer.metricsApiHelper))
            .get(MetricsViewModel::class.java)
        newMetricInputDialog = NewMetricInputDialog(this)
        newMetricValueDialog = NewMetricValueInputDialog(this)
        observeMetricsState()
    }
    override fun onStart() {
        super.onStart()
        initViews()
        initListeners()
        metricsViewModel?.getMetrics()
    }
    private fun initViews()
    {
        homeSwipeRefreshLayout = findViewById(R.id.home_swipe_refresh_layout)
        addNewMetricFAB = findViewById(R.id.add_new_metric_fab)
        metricsRecyclerView = findViewById(R.id.metrics_recycler_view)
        apiErrorMessage = findViewById(R.id.metrics_api_error_txt)
        loadingProgressBar = findViewById(R.id.api_loading_bar)
        metricsListRecyclerAdapter = MetricsListRecyclerAdapter(appContainer.averageCalculator, onAddNewMetricValueClick = {
            metric, position ->
            addNewMetricValue(metric, position)
        })
        metricsRecyclerView?.adapter = this.metricsListRecyclerAdapter
    }
    private fun initListeners() {
        homeSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            this@HomeActivity.metricsViewModel?.getMetrics()
            homeSwipeRefreshLayout?.isRefreshing = false
        })
        addNewMetricFAB?.setOnClickListener {
            newMetricInputDialog?.showDialog(onAddNewMetric = {
                //Add new metric
                addNewMetric(it)
            })
        }
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

    private fun addNewMetric(name : String)
    {
        metricsViewModel?.addNewMetric(NewMetricBody(name))
    }
    private fun addNewMetricValue(metric : Metric, position : Int)
    {
        newMetricValueDialog?.showDialog(metric.name, onAddNewMetricValue = {
            metricsViewModel?.addNewMetricValue(metric.id, position, NewMetricValueBody(it))
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