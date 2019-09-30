package me.chizobaogbonna.chatty.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import me.chizobaogbonna.chatty.R
import me.chizobaogbonna.chatty.ui.adapter.MessageAdapter
import me.chizobaogbonna.chatty.ui.dialog.DeleteDialogFragment
import me.chizobaogbonna.chatty.utils.InjectorUtils

class HomeFragment : Fragment(), DeleteDialogFragment.OnDeleteClickListener {

    private val viewModelFactory: HomeViewModel.ViewModelFactory by lazy {
        InjectorUtils.provideHomeViewModelFactory(requireContext())
    }
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messageAdapter = MessageAdapter(
            requireContext(),
            onMessageLongClick = { messageId ->
                val fragment = DeleteDialogFragment.newInstance(messageId, clickListener = this)
                fragment.show(childFragmentManager, DeleteDialogFragment::class.java.simpleName)
            },
            onAttachmentLongClick = { messageId, attachmentId ->
                val fragment =
                    DeleteDialogFragment.newInstance(
                        messageId,
                        attachmentId,
                        this
                    )
                fragment.show(childFragmentManager, DeleteDialogFragment::class.java.simpleName)
            }
        )
        messageAdapter.setHasStableIds(true)
        recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
            itemAnimator = DefaultItemAnimator()
        }
        viewModel.pagedMessagesLiveData.observe(viewLifecycleOwner, Observer {
            messageAdapter.submitList(it)
        })
    }


    override fun onDeleteMessageClicked(messageId: Int) {
        viewModel.deleteMessage(messageId)
    }

    override fun onDeleteAttachmentClicked(messageId: Int, attachmentId: String) {
        viewModel.deleteAttachment(messageId, attachmentId)
    }
}
