package com.metrics.performancemetrics.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.metrics.performancemetrics.data.Metric
import com.metrics.performancemetrics.data.MetricValue
import com.metrics.performancemetrics.data.NewMetricBody
import com.metrics.performancemetrics.data.NewMetricValueBody
import com.metrics.performancemetrics.view.OnAddNewMetricValue
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

class MetricsApiHelper(private val metricsApiService : MetricsApiService) {

    suspend fun getAllMetrics() : APIResponse<ArrayList<Metric>> {
        return try {
            metricsApiService.getAllMetrics()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            handleRequestException(throwable)
        }
    }
    suspend fun addNewMetric(newMetricBody: NewMetricBody): APIResponse<Metric> {
        return try {
            metricsApiService.addNewMetric(newMetricBody)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            handleRequestException(throwable)
        }
    }
    suspend fun addNewMetricValue(metricId : Int, newMetricValue: NewMetricValueBody): APIResponse<MetricValue> {
        return try {
            metricsApiService.addNewMetricValue(metricId, newMetricValue)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            handleRequestException(throwable)
        }
    }

    private fun <T> handleRequestException(throwable: Throwable): APIResponse<T> {
        when (throwable) {
            is HttpException -> {
                val errorResponse: APIResponse<Any>? = convertErrorBody(throwable)
                return APIResponse(
                    false,
                    throwable.code(),
                    message = errorResponse?.message ?: "Something went wrong!",
                    null
                )
            }

            is IOException -> {
                return APIResponse(
                    false,
                    503,
                    message = throwable.message ?: "Something went wrong!",
                    null
                )
            }

            else -> {
                return APIResponse(
                    false,
                    52,
                    message = throwable.message ?: "Something went wrong!",
                    null
                )
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): APIResponse<Any>? {
        return try {
            throwable.response()?.errorBody()?.string()?.let {
                val responseType = genericType<APIResponse<Any>>()
                val gson = Gson()
                return gson.fromJson<APIResponse<Any>>(it, responseType)
            }
        } catch (exception: Exception) {
            null
        }
    }
    inline fun <reified T> genericType(): Type = object: TypeToken<T>() {}.type
}