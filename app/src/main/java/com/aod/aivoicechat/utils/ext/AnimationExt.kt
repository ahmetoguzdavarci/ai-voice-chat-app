package com.aod.aivoicechat.utils.ext

import android.view.View

fun View.resetAnim(customAlpha: Float = 0f) {
    animate().cancel()
    alpha = customAlpha
    translationX = 0f
    translationY = 0f
}

fun View.slideHorizontal(
    fromLeft: Boolean = true,
    delay: Long = 0L,
    duration: Long = 600L,
    distanceX: Float = 200f
) {
    alpha = 0f
    translationX = if (fromLeft) -distanceX else distanceX

    animate()
        .alpha(1f)
        .translationX(0f)
        .setStartDelay(delay)
        .setDuration(duration)
        .start()
}

fun View.slideVertical(
    fromTop: Boolean = true,
    delay: Long = 0L,
    duration: Long = 600L,
    distanceY: Float = 200f
) {
    alpha = 0f
    translationY = if (fromTop) -distanceY else distanceY
    animate()
        .alpha(1f)
        .translationY(0f)
        .setStartDelay(delay)
        .setDuration(duration)
        .start()
}

fun View.fadeIn(
    delay: Long = 0L,
    duration: Long = 600L,
    startAlpha: Float = 0f
) {
    alpha = startAlpha
    animate()
        .alpha(1f)
        .setStartDelay(delay)
        .setDuration(duration)
        .start()
}