package com.aod.aivoicechat.utils.ext

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

fun View.setOnClickWithScaleAnimation(
    clickDelay: Long = 500L,
    clickDuration: Long = 50L,
    releaseDuration: Long = 100L,
    clickScale: Float = 0.8F,
    releaseScale: Float = 1.0F,
    listener: View.OnClickListener
) {
    setOnScaleAnimationClick(
        listener = listener,
        clickDelay = clickDelay,
        clickAnimation = {
            animate().setDuration(clickDuration).scaleX(clickScale).scaleY(clickScale).start()
        },
        releaseAnimation = {
            animate().setDuration(releaseDuration).scaleX(releaseScale).scaleY(releaseScale).start()
        }
    )
}

private fun View.setOnScaleAnimationClick(
    listener: View.OnClickListener,
    clickDelay: Long,
    clickAnimation: () -> Unit,
    releaseAnimation: () -> Unit
) {
    var lastClickTime = 0L
    var released = false

    setOnTouchListener { _view, _event ->
        when (_event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (SystemClock.elapsedRealtime() - lastClickTime > clickDelay) clickAnimation()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                releaseAnimation()
                if (_event.action == MotionEvent.ACTION_UP &&
                    !released &&
                    SystemClock.elapsedRealtime() - lastClickTime > clickDelay
                ) {
                    listener.onClick(_view)
                    lastClickTime = SystemClock.elapsedRealtime()
                }
                released = false
                _view.performClick()
            }

            MotionEvent.ACTION_MOVE -> {
                val x = _event.x
                val y = _event.y
                released = x < 0 || y < 0 || x > width || y > height
                if (released) releaseAnimation()
            }
        }
        true
    }
}

fun View.setOnClickWithAlphaAnimation(
    clickDelay: Long = 500L,
    flashAlpha: Float = 0.3F,
    listener: View.OnClickListener,
) {
    setOnAlphaAnimationClick(
        listener = listener,
        clickDelay = clickDelay,
        clickAnimation = { this.alpha = flashAlpha },
        releaseAnimation = { this.alpha = 1.0F }
    )
}


private fun View.setOnAlphaAnimationClick(
    listener: View.OnClickListener,
    clickDelay: Long,
    clickAnimation: () -> Unit,
    releaseAnimation: () -> Unit
) {
    var lastClickTime = 0L
    var isReleased = false

    setOnTouchListener { _view, _event ->
        when (_event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (SystemClock.elapsedRealtime() - lastClickTime > clickDelay) clickAnimation()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                releaseAnimation()
                if (_event.action == MotionEvent.ACTION_UP &&
                    !isReleased &&
                    SystemClock.elapsedRealtime() - lastClickTime > clickDelay
                ) {
                    listener.onClick(_view)
                    lastClickTime = SystemClock.elapsedRealtime()
                }
                isReleased = false
                _view.performClick()
            }

            MotionEvent.ACTION_MOVE -> {
                val x = _event.x
                val y = _event.y
                isReleased = x < 0 || y < 0 || x > width || y > height
                if (isReleased) releaseAnimation()
            }
        }
        true
    }
}

fun View.setAlphaAnimation(
    mDuration: Long,
    mDelay: Long,
    mFrom: Float,
    mTo: Float
): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "alpha", mFrom, mTo).apply {
        duration = mDuration
        repeatCount = 0
        startDelay = mDelay
        repeatMode = ObjectAnimator.REVERSE
        addListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    this@setAlphaAnimation.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {

                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            }
        )
    }
}