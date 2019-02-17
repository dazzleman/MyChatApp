package ru.ic218.mychatapp.feature.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_in_message_item.view.*
import kotlinx.android.synthetic.main.view_out_message_item.view.*
import ru.ic218.mychatapp.R
import ru.ic218.mychatapp.common.InitialsView
import ru.ic218.mychatapp.data.db.model.MessageEntity
import ru.ic218.mychatapp.domain.model.PrefRepository
import ru.ic218.mychatapp.domain.model.TypeMessage
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val OWNER_MESSAGE = 1
private const val GUEST_MESSAGE = 2

class MessageAdapter : PagedListAdapter<MessageEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private object DIFF_CALLBACK : DiffUtil.ItemCallback<MessageEntity>() {
            override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
                return oldItem.createTime == newItem.createTime
            }

            override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            OWNER_MESSAGE -> OwnerMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_out_message_item, parent, false)
            )
            else -> GuestMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_in_message_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            OWNER_MESSAGE -> (holder as OwnerMessageViewHolder).bindData(getItem(position))
            else -> (holder as GuestMessageViewHolder).bindData(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.type) {
            TypeMessage.OWNER-> OWNER_MESSAGE
            else -> GUEST_MESSAGE
        }
    }

    inner class OwnerMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(message: MessageEntity?) {
            message?.let {
                itemView.messageOutText.text = it.message
                itemView.messageOutTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(it.createTime))
            }
        }
    }

    inner class GuestMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(message: MessageEntity?) {
            message?.let {
                if(it.ownerName.isNotEmpty()) (itemView.messageInAuthorLogo as InitialsView).populateInitials(it.ownerName)
                itemView.messageInAuthor.text = it.ownerName
                itemView.messageInText.text = it.message
                itemView.messageInTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(it.createTime))
            }
        }
    }
}