package me.chizobaogbonna.chatty.data


class Repository(private val messageDao: MessageDao) {

    fun retrieveMessages() = messageDao.getMessagesAndUsers()
    suspend fun deleteMessage(messageId: Int) = messageDao.deleteMessage(messageId)
    suspend fun deleteAttachment(messageId: Int, attachmentId: String) =
        messageDao.getMessageDeleteAttachmentUpdateMessage(messageId, attachmentId)

    companion object {

        @Volatile
        private var instance: Repository? = null

        fun getInstance(messageDao: MessageDao) =
            instance ?: synchronized(this) {
                instance ?: Repository(messageDao).also { instance = it }
            }
    }
}