package com.metrics.performancemetrics.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.metrics.performancemetrics.average.AverageCalculator
import com.metrics.performancemetrics.R
import com.metrics.performancemetrics.average.MetricAverageTextView
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.createdAtMillis

typealias OnAddNewMetricValueClick = (metric : Metric, position : Int) -> Unit
class MetricsListRecyclerAdapter(private val averageCalculator: AverageCalculator,private val onAddNewMetricValueClick : OnAddNewMetricValueClick) : RecyclerView.Adapter<MetricsListRecyclerAdapter.ViewHolder>() {

    private val metricsList = ArrayList<Metric>()
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.metric_list_item_view, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentMetric = metricsList[position]
        viewHolder.metricNameTxt.text = currentMetric.name
        val metricValuesAverage =
            averageCalculator.calculateAverage(currentMetric.createdAtMillis(), System.currentTimeMillis(), currentMetric.metricValues)
        viewHolder.minuteAvgTxt.setValue(metricValuesAverage.minuteAvg)
        viewHolder.hourAvgTxt.setValue(metricValuesAverage.hourAvg)
        viewHolder.dayAvgTxt.setValue(metricValuesAverage.dayAvg)
        viewHolder.addNewMetricValueBtn.setOnClickListener {
            onAddNewMetricValueClick(currentMetric, position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = metricsList.size

    fun setMetricsList(metrics : ArrayList<Metric>)
    {
        this.metricsList.clear()
        this.metricsList.addAll(metrics)
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val metricNameTxt: TextView
        val minuteAvgTxt : MetricAverageTextView
        val hourAvgTxt : MetricAverageTextView
        val dayAvgTxt : MetricAverageTextView
        val addNewMetricValueBtn : ImageButton
        init {
            metricNameTxt = view.findViewById(R.id.metric_name_txt)
            minuteAvgTxt = view.findViewById(R.id.minute_avg_txt)
            hourAvgTxt = view.findViewById(R.id.hour_avg_txt)
            dayAvgTxt = view.findViewById(R.id.day_avg_txt)
            addNewMetricValueBtn = view.findViewById(R.id.add_new_metric_value_btn)
        }
    }
}