package com.aod.aivoicechat.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val KEY_LANGUAGE = "key_language"

private val Context.sharedPrefs: SharedPreferences
    get() = getSharedPreferences(this.packageName, Context.MODE_PRIVATE)

fun Context.saveLanguage(language: String) {
    sharedPrefs.edit { putString(KEY_LANGUAGE, language) }
}

val Context.getLanguage: String? get() = sharedPrefs.getString(KEY_LANGUAGE, "en")