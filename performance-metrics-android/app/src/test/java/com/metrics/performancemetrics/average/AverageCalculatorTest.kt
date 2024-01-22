package com.metrics.performancemetrics.average

import com.metrics.performancemetrics.data.MetricValue
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AverageCalculatorTest {

    @Test
    fun `calculateAverage with empty metricValues should return empty MetricValuesAverage`() {
        // Arrange
        val calculator = AverageCalculator()
        val startTimeMillis = System.currentTimeMillis()
        val endTimeMillis = System.currentTimeMillis()

        // Act
        val result = calculator.calculateAverage(startTimeMillis, endTimeMillis, emptyList())

        // Assert
        assertEquals(MetricValuesAverage(), result)
    }

    @Test
    fun `calculateAverage with a single metricValue should return MetricValuesAverage with the same values`() {
        // Arrange
        val calculator = AverageCalculator()
        val startTimeMillis = System.currentTimeMillis()
        val endTimeMillis = System.currentTimeMillis()

        val metricValue = createMetricValue(123.45, startTimeMillis)
        val metricValues = listOf(metricValue)

        // Act
        val result = calculator.calculateAverage(startTimeMillis, endTimeMillis, metricValues)

        // Assert
        assertEquals(MetricValuesAverage(123.45, 123.45, 123.45), result)
    }

    @Test
    fun `calculateAverage with multiple metricValues should return correct MetricValuesAverage`() {
        // Arrange
        val calculator = AverageCalculator()
        val startTimeMillis = System.currentTimeMillis()
        val endTimeMillis = System.currentTimeMillis() + 3600000 // 1 hour

        val metricValue1 = createMetricValue(100.0, startTimeMillis)
        val metricValue2 = createMetricValue(200.0, startTimeMillis + 1800000) // 30 minutes later
        val metricValue3 = createMetricValue(300.0, startTimeMillis + 3600000) // 1 hour later
        val metricValues = listOf(metricValue1, metricValue2, metricValue3)

        // Act
        val result = calculator.calculateAverage(startTimeMillis, endTimeMillis, metricValues)

        // Assert
        assertEquals(MetricValuesAverage(10.0, 600.0, 14400.0), result)
    }

    private fun createMetricValue(value: Double, createdAtMillis: Long): MetricValue {
        return MetricValue(1, value, Instant.now().toString(), 1)
    }
}
