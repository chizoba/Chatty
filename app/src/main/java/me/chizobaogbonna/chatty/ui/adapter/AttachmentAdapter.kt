package me.chizobaogbonna.chatty.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.attachment_layout.view.*
import me.chizobaogbonna.chatty.R
import me.chizobaogbonna.chatty.domain.models.Attachment

class AttachmentAdapter(
    private val context: Context, private val messageId: Int,
    private val onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit
) :
    ListAdapter<Attachment, AttachmentAdapter.ViewHolder>(AttachmentDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(context, it, messageId, onAttachmentLongClick)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.attachment_layout, parent, false)
        return ViewHolder(view)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var urlImageView = view.urlImageView
        private var titleView = view.titleView
        private var thumbnailImageView = view.thumbnailImageView

        fun bind(
            context: Context, attachment: Attachment, messageId: Int,
            onAttachmentLongClick: (messageId: Int, attachmentId: String) -> Unit
        ) {
            titleView.text = attachment.title
            loadImageToView(context, attachment.url, urlImageView)
            loadImageToView(context, attachment.thumbnailUrl, thumbnailImageView)

            itemView.setOnLongClickListener {
                onAttachmentLongClick(messageId, attachment.id)
                return@setOnLongClickListener false
            }
        }

        private fun loadImageToView(context: Context, url: String, imageView: ImageView) {
            Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)
        }

    }

    class AttachmentDiffCallback : DiffUtil.ItemCallback<Attachment>() {

        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
            return oldItem == newItem
        }
    }
}