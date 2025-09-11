package com.aod.aivoicechat.utils

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.aod.aivoicechat.utils.ext.isNotNull
import java.util.Locale

object TTSManager {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private var pendingListener: UtteranceProgressListener? = null

    fun init(context: Context, onReady: (() -> Unit)? = null) {
        if (tts.isNotNull()) return
        tts = TextToSpeech(
            context.applicationContext,
            TextToSpeech.OnInitListener { status ->
                isInitialized = (status == TextToSpeech.SUCCESS)
                if (isInitialized) {
                    setLanguage(context.getLanguage ?: Locale.getDefault().language)
                    pendingListener?.let { tts?.setOnUtteranceProgressListener(it) }
                    onReady?.invoke()
                }
            },
            context.getTTSEngine()
        )
    }

    fun setListener(listener: UtteranceProgressListener) {
        pendingListener = listener
        if (isInitialized) {
            tts?.setOnUtteranceProgressListener(listener)
        }
    }

    fun speak(
        text: String,
        queueMode: Int = TextToSpeech.QUEUE_FLUSH,
        utteranceId: String? = null
    ) {
        if (isInitialized.not()) return

        val params = Bundle().apply {
            putString(
                TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                utteranceId ?: "default_utterance"
            )
        }

        tts?.speak(text, queueMode, params, utteranceId)
    }

    fun stopSpeak() {
        tts?.stop()
    }

    fun isSpeaking(): Boolean = tts?.isSpeaking == true

    fun isLanguageAvailable(language: Locale): Boolean {
        return (tts?.isLanguageAvailable(language) == TextToSpeech.LANG_AVAILABLE)
    }

    fun setLanguage(language: Locale) {
        tts?.language = language
    }

    fun setLanguage(language: String) {
        tts?.language = Locale.forLanguageTag(language)
    }

    fun shutdown() {
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}