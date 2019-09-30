package me.chizobaogbonna.chatty.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class MessageAndUserData(
    @Embedded
    val message: Message,

    @Relation(parentColumn = "userId", entityColumn = "id")
    val user: List<User> = emptyList()
)