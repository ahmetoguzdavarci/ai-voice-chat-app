package com.aod.aivoicechat.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aod.aivoicechat.R
import com.aod.aivoicechat.data.TYPING
import com.aod.aivoicechat.databinding.FragmentHomeBinding
import com.aod.aivoicechat.ui.BaseFragment
import com.aod.aivoicechat.ui.settings.language.getAIFirstMessage
import com.aod.aivoicechat.utils.MicPermissionManager
import com.aod.aivoicechat.utils.STTListener
import com.aod.aivoicechat.utils.STTManager
import com.aod.aivoicechat.utils.TTSManager
import com.aod.aivoicechat.utils.ext.printLogD
import com.aod.aivoicechat.utils.ext.showToastMessage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
            homeBtnSettings.setOnClickListener {
                TTSManager.stopSpeak()
                STTManager.stopListening()
                findNavController().goToFragment(R.id.settingsFragment)
            }

            btnMic.setOnClickListener {
                TTSManager.stopSpeak()

                MicPermissionManager.check(
                    onGranted = {
                        Handler(Looper.getMainLooper()).postDelayed({ startSTT() }, 500)
                    },
                    onDenied = { permanentlyDenied ->
                        if (permanentlyDenied)
                            MicPermissionManager.openAppSettings(requireContext())
                        else showToastMessage(R.string.mic_permission_message_1)
                    }
                )
            }

            animationWave.setOnClickListener { stopSTT() }
        }
    }

    private fun setChatAdapter() {
        binding.apply {
            AdapterChat(
                mContext = requireContext(),
                onClick = { _position, _text ->
                    if (TTSManager.isSpeaking()) TTSManager.stopSpeak()
                    else TTSManager.speak(text = _text)
                }
            ).run {
                chatAdapter = this
                chatRecyclerview.setHasFixedSize(true)
                chatRecyclerview.adapter = this
            }
        }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                chatViewModel.setFirstAssistantMessage(text = requireContext().getAIFirstMessage())

                chatViewModel._messages.collectLatest { list ->
                    if (list.isEmpty()) return@collectLatest

                    chatAdapter.setMessages(list)
                    binding.chatRecyclerview.scrollToPosition(list.lastIndex)

                    list.last().content.let {
                        if (it == TYPING || it.isBlank()) return@let

                        if (chatViewModel._readyTTS.value == true) TTSManager.speak(text = it)
                        else {
                            val once = object : androidx.lifecycle.Observer<Boolean> {
                                override fun onChanged(value: Boolean) {
                                    if (value) {
                                        TTSManager.speak(text = it)
                                        chatViewModel._readyTTS.removeObserver(this)
                                    }
                                }
                            }
                            chatViewModel._readyTTS.observe(viewLifecycleOwner, once)
                        }
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

    override fun onReady() {}

    override fun onPartial(text: String) {}

    override fun onFinal(text: String) {
        binding.mUserSpeaking = false
        chatViewModel.onUserSpeaking(userText = text)
    }

    override fun onError(code: Int) {
        binding.mUserSpeaking = false
        getString(R.string.mic_warning_1).printLogD("STT mic")
    }

    override fun onDestroyView() {
        STTManager.stopListening()
        TTSManager.stopSpeak()
        super.onDestroyView()
    }
}