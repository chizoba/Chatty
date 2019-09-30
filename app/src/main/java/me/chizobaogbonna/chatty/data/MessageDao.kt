package me.chizobaogbonna.chatty.data

import androidx.paging.DataSource
import androidx.room.*
import me.chizobaogbonna.chatty.domain.models.Message
import me.chizobaogbonna.chatty.domain.models.MessageAndUserData


@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<Message>)

    @Query("SELECT * FROM messages")
    fun getMessages(): DataSource.Factory<Int, Message>

    @Transaction
    @Query("SELECT * FROM messages WHERE userId IN (SELECT DISTINCT(id) FROM users)")
    fun getMessagesAndUsers(): DataSource.Factory<Int, MessageAndUserData>

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)

    @Transaction
    suspend fun getMessageDeleteAttachmentUpdateMessage(messageId: Int, attachmentId: String) {
        val message = getMessage(messageId)
        val arrayList = ArrayList(message.attachments)
        val iterator = arrayList.iterator()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value.id == attachmentId) {
                iterator.remove()
            }
        }
        val newMessage = Message(arrayList, message.content, message.id, message.userId)
        updateMessage(newMessage)
    }

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessage(messageId: Int): Message

    @Update//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMessage(message: Message)

}