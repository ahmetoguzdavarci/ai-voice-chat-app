package com.aod.aivoicechat.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aod.aivoicechat.viewmodels.ChatViewModel

abstract class BaseActivity<Binding : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    AppCompatActivity() {
    lateinit var binding: Binding

    val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<Binding>(this, layoutResId)
    }
}