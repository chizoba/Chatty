package me.chizobaogbonna.chatty.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.delete_dialog_fragment.*
import me.chizobaogbonna.chatty.R


const val MESSAGE_ID = "messageId"
const val ATTACHMENT_ID = "attachmentId"

class DeleteDialogFragment : AppCompatDialogFragment() {

    private var clickListener: OnDeleteClickListener? = null

    companion object {
        fun newInstance(
            messageId: Int,
            attachmentId: String? = null,
            clickListener: OnDeleteClickListener
        ): DeleteDialogFragment {
            val fragment = DeleteDialogFragment()
            fragment.arguments = bundleOf(MESSAGE_ID to messageId, ATTACHMENT_ID to attachmentId)
            fragment.clickListener = clickListener
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.delete_dialog_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if (it[ATTACHMENT_ID] == null) {
                deleteView.text = getString(me.chizobaogbonna.chatty.R.string.delete_message)
            } else {
                deleteView.text = getString(me.chizobaogbonna.chatty.R.string.delete_attachment)
            }
        }

        deleteButton.setOnClickListener {
            arguments?.let {
                if (it[ATTACHMENT_ID] == null) {
                    clickListener?.onDeleteMessageClicked(it.getInt(MESSAGE_ID))
                } else {
                    clickListener?.onDeleteAttachmentClicked(
                        it.getInt(MESSAGE_ID),
                        it.getString(ATTACHMENT_ID)!!
                    )
                }
            }
            dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface OnDeleteClickListener {
        fun onDeleteMessageClicked(messageId: Int)
        fun onDeleteAttachmentClicked(messageId: Int, attachmentId: String)
    }
}