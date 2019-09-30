package me.chizobaogbonna.chatty.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import me.chizobaogbonna.chatty.domain.models.Attachment

class Converters {

    @TypeConverter
    fun attachmentsToJson(value: List<Attachment>? = listOf()): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToAttachments(value: String): List<Attachment>? {
        var attachment = arrayOf<Attachment>()
        if (value != "null") {
            attachment = Gson().fromJson(value, Array<Attachment>::class.java) as Array<Attachment>
        }
        return attachment.toList()
    }

}