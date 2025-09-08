package com.aod.aivoicechat.data.repository

import com.aod.aivoicechat.data.api.ApiClient
import com.aod.aivoicechat.data.api.ApiRequest
import com.aod.aivoicechat.data.model.ChatRequest
import com.aod.aivoicechat.data.model.ChatResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import retrofit2.HttpException
import java.io.IOException

sealed interface ApiResult<out T> {
    data object Loading : ApiResult<Nothing>
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val message: String, val cause: Throwable? = null) : ApiResult<Nothing>
}

class ApiRepository(
    private val api: ApiRequest = ApiClient.api(),
    private val io: CoroutineDispatcher = Dispatchers.IO
) {
    fun sendRequest(request: ChatRequest): Flow<ApiResult<ChatResponse>> =
        flow {
            emit(ApiResult.Loading)
            val response = api.postUserMessage(request)
            emit(ApiResult.Success(response))
        }
            .retryWhen { cause, attempt ->
                val retry = cause is IOException || (cause is HttpException && cause.code() >= 500)
                if (retry && attempt < 2) {
                    delay((attempt + 1) * 600L)
                    true
                } else false
            }
            .catch { e ->
                val msg = when (e) {
                    is HttpException -> "HTTP ${e.code()} ${e.message()}"
                    is IOException -> "Connection Error"
                    else -> e.message ?: "Unknown Error"
                }
                emit(ApiResult.Error(msg, e))
            }
            .flowOn(io)
}