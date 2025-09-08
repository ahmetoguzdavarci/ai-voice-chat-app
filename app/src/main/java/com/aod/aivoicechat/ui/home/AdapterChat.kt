package com.aod.aivoicechat.ui.home

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aod.aivoicechat.data.ROLE_ASSISTANT
import com.aod.aivoicechat.data.TYPING
import com.aod.aivoicechat.data.model.Message
import com.aod.aivoicechat.databinding.ItemChatAiBinding
import com.aod.aivoicechat.databinding.ItemChatUserBinding
import com.aod.aivoicechat.utils.ext.printLogD

class AdapterChat(private val mContext: Context, val onClick: (Int, String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class UserViewHolder(val binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class AIViewHolder(val binding: ItemChatAiBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var messages: List<Message> = mutableListOf()

    fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_AI = 1
        private const val TYPE_USER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].role == ROLE_ASSISTANT) TYPE_AI else TYPE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_AI -> {
                ItemChatAiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { AIViewHolder(this) }
            }

            TYPE_USER -> {
                ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { UserViewHolder(this) }
            }

            else -> {
                ("Unknown type").printLogD(tag = javaClass.simpleName)
                error("Unknown type")
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val msg = messages[position]
        when (holder.itemViewType) {
            TYPE_AI -> {
                (holder as AIViewHolder).binding.apply {
                    mAdapter = this@AdapterChat
                    isTyping = (msg.content == TYPING)
                    itemTextAi.movementMethod = ScrollingMovementMethod()
                    mMessage = msg
                    mPosition = position
                }
            }

            TYPE_USER -> {
                (holder as UserViewHolder).binding.apply {
                    mAdapter = this@AdapterChat
                    itemTextHuman.movementMethod = ScrollingMovementMethod()
                    mMessage = msg
                    mPosition = position
                }
            }
        }
    }

    override fun getItemCount(): Int = messages.size
}