package com.aod.aivoicechat.data.api

import com.aod.aivoicechat.utils.RemoteConfig
import com.aod.aivoicechat.utils.isDebug
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.openai.com/"
    private const val AUTH_HEADER = "Authorization"

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val API_KEY = RemoteConfig.getApiKey()
                val request = chain.request().newBuilder()
                    .apply { if (API_KEY.isNotEmpty()) header(AUTH_HEADER, "Bearer $API_KEY") }
                    .build()
                chain.proceed(request)
            }
            .followRedirects(followRedirects = false)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .apply {
                if (isDebug) {
                    val logger =
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    addInterceptor(logger)
                }
            }
            .build()
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