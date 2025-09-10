package com.aod.aivoicechat.ui.settings.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.aod.aivoicechat.databinding.ItemLanguageBinding

class AdapterLanguage(private val mContext: Context, private val items: List<LanguageItem>) :
    ArrayAdapter<LanguageItem>(mContext, 0, items) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bind(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bind(position, convertView, parent)
    }

    private fun bind(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemLanguageBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            DataBindingUtil.getBinding<ItemLanguageBinding>(convertView)
                ?: ItemLanguageBinding.inflate(LayoutInflater.from(context), parent, false)
        }
        binding.item = items[position]
        return binding.root
    }

    override fun getItem(position: Int): LanguageItem? = items.getOrNull(position)
}