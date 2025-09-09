package com.aod.aivoicechat.activities

import android.os.Bundle
import com.aod.aivoicechat.R
import com.aod.aivoicechat.databinding.ActivityWarningBinding

class WarningActivity :
    BaseActivity<ActivityWarningBinding>(layoutResId = R.layout.activity_warning) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setItemClick()
    }

    private fun setItemClick() {
        binding.warningBtnClose.setOnClickListener { this.finish() }
    }
}