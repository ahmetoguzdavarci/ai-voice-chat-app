package com.aod.aivoicechat.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.aod.aivoicechat.R
import com.aod.aivoicechat.databinding.FragmentHomeBinding
import com.aod.aivoicechat.ui.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(layoutResId = R.layout.fragment_home) {

    private lateinit var chatAdapterChat: AdapterChat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setItemClick()
    }

    private fun setItemClick() {
        binding.apply {
            btnMic.setOnClickListener {
                isSpeaking = true
                //TODO test
                Handler(Looper.getMainLooper()).postDelayed({ isSpeaking = false }, 1_000L)
                chatViewModel.sendMessage("hello, this is a test message")
            }
        }
    }
}