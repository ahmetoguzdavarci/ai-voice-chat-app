package com.aod.aivoicechat.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aod.aivoicechat.R
import com.aod.aivoicechat.databinding.ActivityStartBinding
import com.aod.aivoicechat.utils.ext.fadeIn
import com.aod.aivoicechat.utils.ext.hideSystemUI
import com.aod.aivoicechat.utils.ext.showDarkTextInStatusBar
import com.aod.aivoicechat.utils.ext.slideVertical
import com.aod.aivoicechat.utils.ext.startActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    private val animHandler = Handler(Looper.getMainLooper())

    private val animDelay = 3_000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)

        hideSystemUI()
        showDarkTextInStatusBar(false)
    }

    override fun onResume() {
        super.onResume()

        setPageAnimation()
        setAutoNextPage()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setPageAnimation() {
        binding.apply {
            splashImage.slideVertical(delay = 0L, duration = 1_000L, distanceY = 400f)
            splashText1.fadeIn(delay = 1_000L, duration = 1_000L, startAlpha = 0f)
        }
    }

    private fun setAutoNextPage() {
        animHandler.postDelayed(
            { startActivity(activityClass = MainActivity::class.java, finish = true) },
            animDelay
        )
    }
}