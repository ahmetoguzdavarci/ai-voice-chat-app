package com.aod.aivoicechat.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

object RemoteConfig {
    private const val CHATGPT_API_KEY = "chatGPT_api_key"
    private const val CHATGPT_API_ENABLE = "chatGPT_api_enable"

    private inline val rc: FirebaseRemoteConfig get() = Firebase.remoteConfig

    @Volatile
    private var loaded = false

    fun fetch() {
        rc.setConfigSettingsAsync(
            remoteConfigSettings { minimumFetchIntervalInSeconds = if (isDebug) 0 else 3_600L }
        )

        rc.setDefaultsAsync(
            mapOf(
                CHATGPT_API_KEY to "",
                CHATGPT_API_ENABLE to false
            )
        )

        rc.fetchAndActivate().addOnCompleteListener { t -> loaded = t.isSuccessful }
    }

    fun getApiKey(): String = rc.getString(CHATGPT_API_KEY)

    fun isApiEnable(): Boolean = rc.getBoolean(CHATGPT_API_ENABLE)

    fun isApiReady(): Boolean {
        val apikey = rc.getValue(CHATGPT_API_KEY)
        val apiEnable = rc.getValue(CHATGPT_API_ENABLE)

        val apiKeyOk = apikey.asString().trim().isNotEmpty()
        val apiEnableOk = apiEnable.asBoolean()

        val notDefault =
            (apikey.source != FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT) && (apiEnable.source != FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT)

        return (loaded && apiKeyOk && apiEnableOk && notDefault)
    }
}