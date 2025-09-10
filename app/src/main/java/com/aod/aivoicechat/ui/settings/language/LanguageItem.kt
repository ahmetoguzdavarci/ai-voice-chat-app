package com.aod.aivoicechat.ui.settings.language

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.aod.aivoicechat.R
import com.aod.aivoicechat.utils.getLanguage

@Keep
enum class LanguageItem(
    @StringRes val languageTitle: Int,
    @StringRes val languageCode: Int,
    @DrawableRes val languageFlag: Int,
    @StringRes val welcomeMessage: Int
) {
    ENGLISH(
        languageTitle = R.string.title_en,
        languageCode = R.string.code_en,
        languageFlag = R.drawable.flag_united_kingdom,
        welcomeMessage = R.string.ai_first_message_en
    ),
    TURKISH(
        languageTitle = R.string.title_tr,
        languageCode = R.string.code_tr,
        languageFlag = R.drawable.flag_turkey,
        welcomeMessage = R.string.ai_first_message_tr
    ),
    FRENCH(
        languageTitle = R.string.title_fr,
        languageCode = R.string.code_fr,
        languageFlag = R.drawable.flag_france,
        welcomeMessage = R.string.ai_first_message_fr
    ),
    GERMAN(
        languageTitle = R.string.title_de,
        languageCode = R.string.code_de,
        languageFlag = R.drawable.flag_germany,
        welcomeMessage = R.string.ai_first_message_de
    )
}

fun Context.getAIFirstMessage(): String {
    return when (getLanguage) {
        "en" -> getString(LanguageItem.ENGLISH.welcomeMessage)
        "tr" -> getString(LanguageItem.TURKISH.welcomeMessage)
        "fr" -> getString(LanguageItem.FRENCH.welcomeMessage)
        "de" -> getString(LanguageItem.GERMAN.welcomeMessage)
        else -> getString(R.string.ai_first_message)
    }
}