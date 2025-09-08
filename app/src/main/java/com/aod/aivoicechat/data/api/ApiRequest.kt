package com.aod.aivoicechat.data.api

import com.aod.aivoicechat.data.model.ChatRequest
import com.aod.aivoicechat.data.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiRequest {
    @POST("v1/chat/completions")
    suspend fun postUserMessage(@Body request: ChatRequest): ChatResponse
}