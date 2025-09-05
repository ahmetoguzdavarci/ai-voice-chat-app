package com.aod.aivoicechat.utils.ext

import android.util.Log

private const val AI_VOICE_CHAT = "AI_VOICE_CHAT_"

fun String.printLogD(tag: String = "") {
    Log.d(AI_VOICE_CHAT + tag, this)
}

fun String.printLogE(tag: String = "") {
    Log.e(AI_VOICE_CHAT + tag, this)
}

fun String.printLogI(tag: String = "") {
    Log.i(AI_VOICE_CHAT + tag, this)
}

fun String.printLogW(tag: String = "") {
    Log.w(AI_VOICE_CHAT + tag, this)
}