package ru.ic218.mychatapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import ru.ic218.mychatapp.data.db.AppDatabase
import ru.ic218.mychatapp.data.db.model.MessageEntity
import ru.ic218.mychatapp.domain.interfaces.MessageRepository
import ru.ic218.mychatapp.domain.interfaces.FirebaseRepository
import ru.ic218.mychatapp.domain.model.PrefRepository
import ru.ic218.mychatapp.domain.model.TypeMessage
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

private const val PAGE_SIZE = 20
private const val INITIAL_LOAD_SIZE_HINT = 20

class MessageRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val db: AppDatabase,
    private val prefRepository: PrefRepository
) : MessageRepository {

    override fun getMessagesLiveData(): LiveData<PagedList<MessageEntity>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(db.messageDao().getChats(), pagedListConfig).build()
    }

    override fun insertMessagesToDb(value: QuerySnapshot) {
        value.documents.forEach {
            it.toObject(MessageEntity::class.java)?.let {
                it.type = if (it.ownerId != prefRepository.ownerId) TypeMessage.GUEST else TypeMessage.OWNER
                db.messageDao().insertMessage(it)
            }
        }
    }

    override fun sendMessage(messageText: String): Completable {
        return Completable.fromAction {
            val model = MessageEntity(
                Random.nextLong(),
                prefRepository.ownerId,
                prefRepository.ownerName,
                messageText,
                Date().time,
                TypeMessage.OWNER
            )
            db.messageDao().insertMessage(model)
            firebaseRepository.getMessageReference().add(model)
        }
            .subscribeOn(Schedulers.io())
    }
}