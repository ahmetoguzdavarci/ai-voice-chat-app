package com.aod.aivoicechat.ui.home

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aod.aivoicechat.data.model.Message
import com.aod.aivoicechat.databinding.ItemChatAiBinding
import com.aod.aivoicechat.databinding.ItemChatHumanBinding

class AdapterChat(private val context: Context, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HumanViewHolder(val binding: ItemChatHumanBinding) :
        RecyclerView.ViewHolder(binding.root)   //1

    inner class AIViewHolder(val binding: ItemChatAiBinding) :
        RecyclerView.ViewHolder(binding.root)   //2

    private var messages: List<Message> = mutableListOf()

    fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].role == "assistant") 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                ItemChatHumanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { HumanViewHolder(this) }
            }

            else -> {
                ItemChatAiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { AIViewHolder(this) }
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder.itemViewType) {
            1 -> {
                (holder as HumanViewHolder).binding.apply {
                    itemTextHuman.movementMethod = ScrollingMovementMethod()
                    mMessage = messages[position]
                    mPosition = position
                }
            }

            else -> {
                (holder as AIViewHolder).binding.apply {
                    itemTextAi.movementMethod = ScrollingMovementMethod()
                    mMessage = messages[position]
                    mPosition = position
                }
            }
        }
    }

    override fun getItemCount(): Int = messages.size
}