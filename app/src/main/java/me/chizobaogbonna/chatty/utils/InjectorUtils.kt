package me.chizobaogbonna.chatty.utils

import android.content.Context
import me.chizobaogbonna.chatty.data.AppDatabase
import me.chizobaogbonna.chatty.data.Repository
import me.chizobaogbonna.chatty.ui.home.HomeViewModel

object InjectorUtils {

    fun provideHomeViewModelFactory(context: Context): HomeViewModel.ViewModelFactory {
        val repository = getRepository(context)
        return HomeViewModel.ViewModelFactory(repository)
    }

    private fun getRepository(context: Context): Repository {
        val database = AppDatabase.getInstance(context.applicationContext)
        return Repository.getInstance(
            database.messageDao()
        )
    }
}