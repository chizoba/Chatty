package me.chizobaogbonna.chatty.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.my_message_layout.view.attachmentRecyclerView
import kotlinx.android.synthetic.main.my_message_layout.view.messageBodyView
import kotlinx.android.synthetic.main.my_message_layout.view.senderLabel
import kotlinx.android.synthetic.main.others_message_layout.view.*
import me.chizobaogbonna.chatty.R
import me.chizobaogbonna.chatty.domain.models.MessageAndUserData

const val SESSION_USER_ITEM_VIEW = 1
const val OTHER_USERS_ITEM_VIEW = 2

class MessageAdapter(
    private val context: Context, val onMessageLongClick: (messageId: Int) -> Unit,
    val onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit
) :
    PagedListAdapter<MessageAndUserData, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            SESSION_USER_ITEM_VIEW -> getItem(position)?.let { message ->
                (holder as SessionUserViewHolder).bind(
                    context,
                    onMessageLongClick,
                    onAttachmentLongClick,
                    message
                )
            }
            OTHER_USERS_ITEM_VIEW -> getItem(position)?.let { message ->
                (holder as OtherUsersViewHolder).bind(
                    context,
                    onMessageLongClick,
                    onAttachmentLongClick,
                    message
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            SESSION_USER_ITEM_VIEW -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.my_message_layout, parent, false)
                viewHolder = SessionUserViewHolder(view)
            }
            OTHER_USERS_ITEM_VIEW -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.others_message_layout, parent, false)
                viewHolder =
                    OtherUsersViewHolder(view)
            }
        }
        return viewHolder
    }


    class SessionUserViewHolder(view: View) : BaseViewHolder(view) {

        private var messageBodyView = view.messageBodyView
        private var senderLabel = view.senderLabel
        private var attachmentRecyclerView = view.attachmentRecyclerView


        override fun bind(
            context: Context,
            onMessageLongClick: (messageId: Int) -> Unit,
            onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit,
            data: MessageAndUserData
        ) {
            messageBodyView.text = data.message.content

            setupAttachmentAdapter(context, attachmentRecyclerView, data, onAttachmentLongClick)

            setupMessageLongClickListener(onMessageLongClick, data)
        }

    }

    class OtherUsersViewHolder(view: View) : BaseViewHolder(view) {

        private var messageBodyView = view.messageBodyView
        private var senderLabel = view.senderLabel
        private var attachmentRecyclerView = view.attachmentRecyclerView
        private var avatar = view.avatar

        override fun bind(
            context: Context,
            onMessageLongClick: (messageId: Int) -> Unit,
            onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit,
            data: MessageAndUserData
        ) {

            Glide
                .with(context)
                .load(data.user[0].avatarId)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(avatar)

            senderLabel.text = data.user[0].name

            messageBodyView.text = data.message.content

            setupAttachmentAdapter(context, attachmentRecyclerView, data, onAttachmentLongClick)

            setupMessageLongClickListener(onMessageLongClick, data)

        }

    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(
            context: Context,
            onMessageLongClick: (messageId: Int) -> Unit,
            onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit,
            data: MessageAndUserData
        )

        fun setupAttachmentAdapter(
            context: Context,
            attachmentRecyclerView: RecyclerView,
            data: MessageAndUserData,
            onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit
        ) {
            if (data.message.attachments != null) {

                val attachmentAdapter =
                    AttachmentAdapter(context, data.message.id, onAttachmentLongClick)
                attachmentAdapter.setHasStableIds(true)
                attachmentRecyclerView.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = attachmentAdapter
                    itemAnimator = DefaultItemAnimator()
                }
                attachmentAdapter.submitList(data.message.attachments)
            }
        }

        fun setupMessageLongClickListener(
            onMessageLongClick: (messageId: Int) -> Unit,
            data: MessageAndUserData
        ) {
            itemView.setOnLongClickListener {
                onMessageLongClick(data.message.id)
                return@setOnLongClickListener false
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val data = getItem(position)
        return data?.message?.id!!.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return if (data?.message?.userId == 1) {
            SESSION_USER_ITEM_VIEW
        } else {
            OTHER_USERS_ITEM_VIEW
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<MessageAndUserData>() {

        override fun areItemsTheSame(
            oldItem: MessageAndUserData,
            newItem: MessageAndUserData
        ): Boolean {
            return oldItem.message.id == newItem.message.id
        }

        override fun areContentsTheSame(
            oldItem: MessageAndUserData,
            newItem: MessageAndUserData
        ): Boolean {
            return oldItem == newItem
        }
    }
}