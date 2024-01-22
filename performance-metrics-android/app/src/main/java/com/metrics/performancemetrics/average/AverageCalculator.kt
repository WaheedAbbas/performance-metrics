package com.metrics.performancemetrics.average

import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.createdAtMillis
class AverageCalculator {

    fun calculateAverage(metricValues: List<MetricValue>): MetricValuesAverage {
        var sum = 0.0
        val startTimeMillis: Long = metricValues.firstOrNull()?.createdAtMillis() ?: return MetricValuesAverage()
        val endTimeMillis: Long = metricValues.lastOrNull()?.createdAtMillis() ?: return MetricValuesAverage()

        if(metricValues.size == 1){
            return MetricValuesAverage(metricValues.first().value, metricValues.first().value, metricValues.first().value)
        }

        for (metricValue in metricValues) {
            val createdAtMillis = metricValue.createdAtMillis()
            if (createdAtMillis in startTimeMillis..endTimeMillis) {
                sum += metricValue.value
            }
        }
        val elapsedMillis = endTimeMillis - startTimeMillis

        // Calculate the time elapsed in minutes
        val timeElapsedMinutes = elapsedMillis / (60 * 1000).toDouble()
        val timeElapsedHours = elapsedMillis / (60 * 60 * 1000).toDouble()
        val timeElapsedDays = elapsedMillis / (60 * 60 * 24 * 1000).toDouble()

        val minuteAverage = sum / timeElapsedMinutes
        val hourAverage = sum / timeElapsedHours
        val dayAverage = sum / timeElapsedDays
        return MetricValuesAverage(minuteAverage, hourAverage, dayAverage)
    }
}
