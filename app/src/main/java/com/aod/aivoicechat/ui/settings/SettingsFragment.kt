package com.aod.aivoicechat.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.annotation.DrawableRes
import androidx.navigation.fragment.findNavController
import com.aod.aivoicechat.R
import com.aod.aivoicechat.databinding.FragmentSettingsBinding
import com.aod.aivoicechat.ui.BaseFragment
import com.aod.aivoicechat.ui.settings.language.AdapterLanguage
import com.aod.aivoicechat.ui.settings.language.LanguageItem
import com.aod.aivoicechat.utils.STTManager
import com.aod.aivoicechat.utils.TTSManager
import com.aod.aivoicechat.utils.getLanguage
import com.aod.aivoicechat.utils.saveLanguage
import com.aod.aivoicechat.utils.toLocale
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLanguageAdapter()
        setItemClick()
    }

    private fun setItemClick() {
        binding.settingsBtnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setLanguageAdapter() {
        val items = LanguageItem.entries
        val adapter = AdapterLanguage(requireContext(), items)
        binding.apply {
            settingsDropLanguage.setAdapter(adapter)

            val lang = requireContext().getLanguage ?: Locale.getDefault().language

            val defaultItem =
                items.find { lang == getString(it.languageCode) } ?: LanguageItem.ENGLISH

            settingsDropLanguage.setText(getString(defaultItem.languageTitle), false)
            settingsContainerDropdown.updateEndFlag(defaultItem.languageFlag, settingsDropLanguage)

            settingsDropLanguage.setOnItemClickListener { _, _, pos, _ ->
                val selected = items[pos]
                settingsDropLanguage.setText(getString(selected.languageTitle), false)
                settingsContainerDropdown.updateEndFlag(selected.languageFlag, settingsDropLanguage)

                requireContext().saveLanguage(getString(selected.languageCode))

                TTSManager.setLanguage(getString(selected.languageCode))

                STTManager.shutdown()
                STTManager.init(
                    context = requireContext(),
                    language = getString(selected.languageCode).toLocale()!!,
                    preferOffline = false,
                    enablePartial = true,
                    onReady = null
                )
            }
        }
    }

    fun TextInputLayout.updateEndFlag(@DrawableRes resId: Int, drop: AutoCompleteTextView) {
        endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
        setEndIconTintList(null)
        setEndIconDrawable(resId)
        setEndIconOnClickListener { drop.showDropDown() }
    }
}