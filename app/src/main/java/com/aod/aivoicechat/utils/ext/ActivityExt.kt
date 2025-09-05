package com.aod.aivoicechat.utils.ext

import android.app.Activity
import android.content.Intent

fun Any?.isNull() = this == null

fun Any?.isNotNull() = this != null

fun Any?.isTrue() = this == true

fun Any?.isFalse() = this == false

fun Activity.startActivity(
    activityClass: Class<*>,
    flags: Int? = Intent.FLAG_ACTIVITY_CLEAR_TOP,
    finish: Boolean? = null
) {
    Intent(this, activityClass).also { intent ->
        flags?.let { intent.flags = it }
        this.startActivity(intent)
        finish?.let { if (it) this.finish() }
    }
}
