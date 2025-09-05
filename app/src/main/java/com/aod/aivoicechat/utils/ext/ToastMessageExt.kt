package com.aod.aivoicechat.utils.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.showToastMessage(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Fragment.showToastMessage(message: String, length: Int = Toast.LENGTH_SHORT) {
    context?.showToastMessage(message, length)
}

fun Context.showToastMessage(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Fragment.showToastMessage(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) {
    context?.showToastMessage(message, length)
}