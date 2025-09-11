package com.aod.aivoicechat.data.model

import androidx.annotation.Keep
import com.aod.aivoicechat.data.MODEL_CHAT_GPT_5

@Keep
data class ChatRequest(
    val model: String = MODEL_CHAT_GPT_5,
    val messages: List<Message>
)