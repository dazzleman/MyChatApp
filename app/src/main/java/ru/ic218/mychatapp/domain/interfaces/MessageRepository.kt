package ru.ic218.mychatapp.domain.interfaces

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import ru.ic218.mychatapp.data.db.model.MessageEntity

interface MessageRepository {

    fun getMessagesLiveData(): LiveData<PagedList<MessageEntity>>
    fun insertMessagesToDb(value: QuerySnapshot)
    fun sendMessage(messageText: String): Completable
}