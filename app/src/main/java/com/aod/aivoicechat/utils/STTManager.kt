package com.aod.aivoicechat.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

interface STTListener {
    fun onReady()
    fun onPartial(text: String)
    fun onFinal(text: String)
    fun onError(code: Int)
}

object STTManager {
    private var recognizer: SpeechRecognizer? = null
    private var isInitialized = false
    private var listener: STTListener? = null

    fun init(
        context: Context,
        language: Locale,
        preferOffline: Boolean = false,
        enablePartial: Boolean = true,
        onReady: (() -> Unit)? = null
    ) {
        if (isInitialized) return

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            listener?.onError(-1)
            return
        }

        recognizer = SpeechRecognizer.createSpeechRecognizer(context.applicationContext).apply {
            setRecognitionListener(
                object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        listener?.onReady()
                        onReady?.invoke()
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}

                    override fun onError(error: Int) {
                        listener?.onError(error)
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        if (!enablePartial) return
                        val text = partialResults
                            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            ?.firstOrNull()
                        if (!text.isNullOrBlank()) listener?.onPartial(text)
                    }

                    override fun onResults(results: Bundle) {
                        val text = results
                            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            ?.firstOrNull()
                        if (!text.isNullOrBlank()) listener?.onFinal(text)
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                }
            )
        }

        this.language = language
        this.preferOffline = preferOffline
        this.enablePartial = enablePartial
        isInitialized = true
    }

    fun setListener(listener: STTListener?) {
        this.listener = listener
    }

    fun setLanguage(language: String) {
        this.language = Locale.forLanguageTag(language)
    }

    fun startListening() {
        if (!isInitialized) return
        recognizer?.startListening(buildIntent())
    }

    fun stopListening() {
        recognizer?.stopListening()
    }

    fun cancel() {
        recognizer?.cancel()
    }

    fun shutdown() {
        recognizer?.destroy()
        recognizer = null
        isInitialized = false
        listener = null
    }

    private var language: Locale = Locale.getDefault()
    private var preferOffline: Boolean = false
    private var enablePartial: Boolean = true

    private fun buildIntent(): Intent =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            val langTag = when(language.language){
                "en" -> "en-US"
                "tr" -> "tr-TR"
                "fr" -> "fr-FR"
                "de" -> "de-DE"
                else -> language.language
            }
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)

            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, enablePartial)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, preferOffline)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
}
