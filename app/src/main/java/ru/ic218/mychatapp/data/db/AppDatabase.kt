package ru.ic218.mychatapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ic218.mychatapp.data.db.dao.MessageDao
import ru.ic218.mychatapp.data.db.model.MessageEntity

@Database(entities = [
    MessageEntity::class
],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun get(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app.db"
                )
                    .build()
            }
            return instance!!
        }
    }

    abstract fun messageDao(): MessageDao
}
