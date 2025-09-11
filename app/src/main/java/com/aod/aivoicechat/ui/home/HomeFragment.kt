package com.aod.aivoicechat.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.UtteranceProgressListener
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
                stopTTS(id = chatViewModel._speakingId.value!!)
                STTManager.stopListening()
                //findNavController().goToFragment(R.id.settingsFragment)
                findNavController().goToFragment(R.id.composeSettingsFragment)
            }

            btnMic.setOnClickListener {
                stopTTS(id = chatViewModel._speakingId.value!!)

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

        TTSManager.setListener(
            object : UtteranceProgressListener() {
                override fun onDone(p0: String?) {
                    chatViewModel.setSpeaking(value = false)
                    chatViewModel.setSpeakingId(value = -1)
                }

                override fun onError(p0: String?) {
                    chatViewModel.setSpeaking(value = false)
                    chatViewModel.setSpeakingId(value = -1)
                }

                override fun onStart(p0: String?) {}
            }
        )
    }

    private fun setChatAdapter() {
        binding.apply {
            AdapterChat(
                mContext = requireContext(),
                viewModel = chatViewModel,
                lifecycleOwner = this@HomeFragment,
                onClick = { _position, _text ->
                    if (TTSManager.isSpeaking()) stopTTS(id = _position)
                    else speakTTS(txt = _text, id = _position)
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

                        if (chatViewModel._readyTTS.value == true) speakTTS(txt = it, list.lastIndex)
                        else {
                            val once = object : androidx.lifecycle.Observer<Boolean> {
                                override fun onChanged(value: Boolean) {
                                    if (value) {
                                        speakTTS(txt = it, id = list.lastIndex)
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

    private fun speakTTS(txt: String, id: Int) {
        TTSManager.speak(text = txt, utteranceId = id.toString())
        chatViewModel.setSpeaking(value = true)
        chatViewModel.setSpeakingId(value = id)
    }

    private fun stopTTS(id: Int) {
        TTSManager.stopSpeak()
        chatViewModel.setSpeaking(value = false)
        chatViewModel.setSpeakingId(value = id)
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