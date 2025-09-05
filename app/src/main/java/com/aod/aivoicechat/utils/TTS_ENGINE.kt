package com.aod.aivoicechat.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech

const val TTS_ENGINE_GOOGLE = "com.google.android.tts"
const val TTS_ENGINE_SAMSUNG = "com.samsung.SMT"

fun Context.getTTSEngine(): String? {
    return if (this.isTTSEngineAvailable(TTS_ENGINE_GOOGLE)) TTS_ENGINE_GOOGLE else null
}

private fun Context.isTTSEngineAvailable(enginePackage: String = TTS_ENGINE_GOOGLE): Boolean {
    val intent = Intent(TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE)
    intent.setPackage(enginePackage)
    return this.packageManager.resolveService(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
}