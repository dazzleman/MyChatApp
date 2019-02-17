package ru.ic218.mychatapp.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.ic218.mychatapp.data.db.converter.TypeMessageConverter
import ru.ic218.mychatapp.domain.model.TypeMessage

@Entity(tableName = "chat")
@TypeConverters(TypeMessageConverter::class)
data class MessageEntity(
    @PrimaryKey
    val id: Long,
    val ownerId: Int,
    val ownerName: String,
    val message: String,
    val createTime: Long,
    var type: TypeMessage = TypeMessage.GUEST
) {
    constructor(): this (0, 0, "", "", 0L)
}