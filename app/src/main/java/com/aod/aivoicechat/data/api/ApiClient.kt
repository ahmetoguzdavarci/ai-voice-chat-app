package com.aod.aivoicechat.data.api

import com.aod.aivoicechat.utils.isDebug
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.openai.com/"
    private const val PROJECT_NAME = "Authorization"
    private val API_KEY = ""
    val PROJECT_KEY = "Bearer $API_KEY"

    private fun getClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(PROJECT_NAME, PROJECT_KEY)
                    .build()
                chain.proceed(request)
            }
            .followRedirects(followRedirects = false)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .apply {
                if (isDebug) {
                    val httpLoginInterceptor = HttpLoggingInterceptor()
                    httpLoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                    httpClient.addInterceptor(httpLoginInterceptor)
                }
            }

        return httpClient.build()
    }

    @Volatile
    private var api: ApiRequest? = null

    @Synchronized
    fun api(): ApiRequest {
        return api ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)
            .also { api = it }
    }
}