package me.chizobaogbonna.chatty.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Data(
    val messages: List<Message>,
    val users: List<User>
)

@Entity(tableName = "messages")
data class Message(
    val attachments: List<Attachment>?,
    val content: String,
    @PrimaryKey val id: Int,
    val userId: Int
)

data class Attachment(
    val id: String,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)

@Entity(tableName = "users")
data class User(
    val avatarId: String,
    @PrimaryKey val id: Int,
    val name: String
)