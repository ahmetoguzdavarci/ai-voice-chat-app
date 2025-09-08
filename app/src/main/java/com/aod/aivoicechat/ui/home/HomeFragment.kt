package com.aod.aivoicechat.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aod.aivoicechat.R
import com.aod.aivoicechat.data.TYPING
import com.aod.aivoicechat.databinding.FragmentHomeBinding
import com.aod.aivoicechat.ui.BaseFragment
import com.aod.aivoicechat.utils.MicPermissionManager
import com.aod.aivoicechat.utils.STTListener
import com.aod.aivoicechat.utils.STTManager
import com.aod.aivoicechat.utils.TTSManager
import com.aod.aivoicechat.utils.ext.showToastMessage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragment : BaseFragment<FragmentHomeBinding>(layoutResId = R.layout.fragment_home),
    STTListener {

    private lateinit var chatAdapter: AdapterChat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        STTManager.setListener(this)

        setChatAdapter()
        setItemClick()

        observeMessages()
    }

    private fun setItemClick() {
        binding.apply {
            btnMic.setOnClickListener {
                TTSManager.stopSpeak()
                MicPermissionManager.check(
                    onGranted = { startSTT() },
                    onDenied = { permanentlyDenied ->
                        if (permanentlyDenied)
                            MicPermissionManager.openAppSettings(requireContext())
                        else showToastMessage(R.string.mic_permission_message_1)
                    }
                )
            }

            animationWave.setOnClickListener {
                stopSTT()
            }
        }
    }

    private fun setChatAdapter() {
        binding.apply {
            AdapterChat(
                mContext = requireContext(),
                onClick = { _position, _text ->
                    TTSManager.speak(text = _text)
                }
            ).run {
                chatAdapter = this
                chatRecyclerview.setHasFixedSize(true)
                chatRecyclerview.adapter = this
            }
        }
    }

    private fun observeMessages() {
        (getText(R.string.ai_first_message) as String).let {
            chatViewModel.setFirstAssistantMessage(text = getText(R.string.ai_first_message) as String)
            TTSManager.speak(text = it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatViewModel._messages.collectLatest { list ->
                    chatAdapter.setMessages(list)
                    binding.chatRecyclerview.scrollToPosition(list.lastIndex)
                    TTSManager.setLanguage(Locale.getDefault())
                    list.last().content.let {
                        if (it == TYPING) return@let
                        TTSManager.speak(text = list.last().content)
                    }
                }
            }
        }
    }

    private fun startSTT() {
        binding.mUserSpeaking = true
        STTManager.startListening()
    }

    private fun stopSTT() {
        binding.mUserSpeaking = false
        STTManager.stopListening()
    }

    override fun onReady() {
        //showToastMessage("onReady")
    }

    override fun onPartial(text: String) {}

    override fun onFinal(text: String) {
        binding.mUserSpeaking = false
        chatViewModel.onUserSpeaking(userText = text)
    }

    override fun onError(code: Int) {
        binding.mUserSpeaking = false
        showToastMessage(R.string.mic_warning_1)
    }

    override fun onDestroyView() {
        STTManager.shutdown()
        TTSManager.shutdown()
        super.onDestroyView()
    }
}