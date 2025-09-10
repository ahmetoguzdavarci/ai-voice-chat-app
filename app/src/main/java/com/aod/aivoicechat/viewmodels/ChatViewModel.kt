package com.aod.aivoicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aod.aivoicechat.data.ROLE_ASSISTANT
import com.aod.aivoicechat.data.ROLE_USER
import com.aod.aivoicechat.data.TYPING
import com.aod.aivoicechat.data.model.ChatRequest
import com.aod.aivoicechat.data.model.Message
import com.aod.aivoicechat.data.repository.ApiRepository
import com.aod.aivoicechat.data.repository.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repo: ApiRepository = ApiRepository()) : ViewModel() {
    private var initialized = false
    private var activeRequestJob: Job? = null
    private var typingIndex: Int? = null

    private val loading = MutableStateFlow(false)
    val _loading: StateFlow<Boolean> = loading

    private val messages = MutableStateFlow<List<Message>>(emptyList())
    val _messages: StateFlow<List<Message>> = messages

    fun setFirstAssistantMessage(text: String) {
        if (initialized) return
        initialized = true
        messages.value = listOf(Message(role = ROLE_ASSISTANT, content = text))
    }

    fun onUserSpeaking(userText: String) {
        if (userText.isBlank()) return

        val list = messages.value.toMutableList()
        list += Message(role = ROLE_USER, content = userText)

        val placeHolder = Message(role = ROLE_ASSISTANT, content = TYPING)
        list += placeHolder
        typingIndex = list.lastIndex

        messages.value = list
        loading.value = true

        val request = buildChatRequest(history = list)

        activeRequestJob?.cancel()
        activeRequestJob = viewModelScope.launch {
            repo.sendRequest(request = request).collect { result ->
                when (result) {
                    is ApiResult.Loading -> Unit
                    is ApiResult.Success -> {
                        val reply = result.data.choices.firstOrNull()?.message?.content.orEmpty()
                        replaceAssistantAt(typingIndex, reply)
                        loading.value = false
                    }

                    is ApiResult.Error -> {
                        replaceAssistantAt(typingIndex, "Error: ${result.message}")
                        loading.value = false
                    }
                }
            }
        }
    }

    private fun replaceAssistantAt(index: Int?, newText: String) {
        if (index == null) return
        val list = messages.value.toMutableList()
        if (index in list.indices) list[index] = Message(role = ROLE_ASSISTANT, content = newText)
        else list += Message(role = ROLE_ASSISTANT, content = newText)

        messages.value = list
    }

    private fun buildChatRequest(history: List<Message>): ChatRequest {
        val msgs = history.map { Message(role = it.role, content = it.content) }
        return ChatRequest(messages = msgs)
    }

    private val readyTTS_ = MutableLiveData(false)
    val _readyTTS: LiveData<Boolean> get() = readyTTS_
    fun setReadyTTS(value: Boolean) {
        readyTTS_.value = value
    }

//    private val skipFirstMessage_ = MutableLiveData(false)
//    val _skipFirstMessage: LiveData<Boolean> get() = skipFirstMessage_
//    fun setSkipFirstMessage(value: Boolean) {
//        skipFirstMessage_.value = value
//    }
}