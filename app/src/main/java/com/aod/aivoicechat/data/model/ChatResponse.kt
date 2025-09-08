package com.aod.aivoicechat.data.model

import androidx.annotation.Keep

@Keep
data class ChatResponse(val choices: List<Choice>)