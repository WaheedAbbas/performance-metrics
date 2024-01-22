package com.metrics.performancemetrics.network

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.time.Instant
import java.util.concurrent.TimeUnit
object APIRetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOKHTTPClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getOKHTTPClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return okHttpClient
            .build()
    }
    val metricsApiService: MetricsApiService = getRetrofitInstance().create(MetricsApiService::class.java)

}