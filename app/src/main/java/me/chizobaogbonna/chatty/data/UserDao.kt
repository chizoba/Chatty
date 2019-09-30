package me.chizobaogbonna.chatty.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import me.chizobaogbonna.chatty.domain.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)
}