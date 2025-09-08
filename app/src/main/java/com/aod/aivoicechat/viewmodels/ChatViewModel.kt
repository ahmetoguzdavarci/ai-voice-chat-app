package com.aod.aivoicechat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aod.aivoicechat.data.api.ApiClient
import com.aod.aivoicechat.data.model.ChatRequest
import com.aod.aivoicechat.data.model.Message
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    var messages = mutableListOf<Message>()
        private set

    private val retrofit = ApiClient.api()

    fun sendMessage(input: String) {
        val userMessage = Message("user", input)
        messages.add(userMessage)

        viewModelScope.launch {
            try {
                val request = ChatRequest(messages = messages.toList())
                val response = retrofit.postUserMessage(request = request)
                val reply = response.choices.firstOrNull()?.message
                if (reply != null) messages.add(reply)
            } catch (e: Exception) {
                messages.add(Message("assistant", "Error : ${e.message}"))
            }
        }
    }
}