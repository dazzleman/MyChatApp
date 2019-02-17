package ru.ic218.mychatapp.data.db.converter

import androidx.room.TypeConverter
import ru.ic218.mychatapp.domain.model.TypeMessage

class TypeMessageConverter {

    @TypeConverter
    fun typeToInt(type: TypeMessage): Int {
        return when(type){
            TypeMessage.OWNER -> 0
            else -> 1
        }
    }

    @TypeConverter
    fun intToType(data: Int): TypeMessage {
        return when(data){
            0 -> TypeMessage.OWNER
            else -> TypeMessage.GUEST
        }
    }
}