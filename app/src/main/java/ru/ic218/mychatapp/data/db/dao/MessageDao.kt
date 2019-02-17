package ru.ic218.mychatapp.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ic218.mychatapp.data.db.model.MessageEntity

@Dao
interface MessageDao {

    @Query("select * from chat order by createTime asc")
    fun getChats(): DataSource.Factory<Int, MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity)
}