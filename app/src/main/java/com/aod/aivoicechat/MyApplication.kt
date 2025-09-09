package com.aod.aivoicechat

import android.app.Application
import com.aod.aivoicechat.utils.RemoteConfig
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        RemoteConfig.fetch()
    }
}