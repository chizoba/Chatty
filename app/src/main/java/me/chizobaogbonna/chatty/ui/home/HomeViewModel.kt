package me.chizobaogbonna.chatty.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import me.chizobaogbonna.chatty.data.Repository
import me.chizobaogbonna.chatty.domain.models.MessageAndUserData

const val PAGE_SIZE = 20

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private var _pagedMessagesLiveData: LiveData<PagedList<MessageAndUserData>>
    val pagedMessagesLiveData: LiveData<PagedList<MessageAndUserData>>
        get() = _pagedMessagesLiveData

    init {
        val dataSourceFactory = repository.retrieveMessages()

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(5)
            .build()
        _pagedMessagesLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }

    fun deleteMessage(messageId: Int) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    fun deleteAttachment(messageId: Int, attachmentId: String) {
        viewModelScope.launch {
            repository.deleteAttachment(messageId, attachmentId)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}