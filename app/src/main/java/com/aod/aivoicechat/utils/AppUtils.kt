package com.aod.aivoicechat.utils

import com.aod.aivoicechat.BuildConfig
import java.util.Locale

val isDebug get() = BuildConfig.DEBUG

fun String.toLocale(): Locale? = Locale.forLanguageTag(this)