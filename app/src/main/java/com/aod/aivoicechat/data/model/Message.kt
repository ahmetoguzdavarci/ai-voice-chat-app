package com.aod.aivoicechat.data.model

import androidx.annotation.Keep

@Keep
data class Message(
    val role: String,
    val content: String
)