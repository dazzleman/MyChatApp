package ru.ic218.mychatapp.feature.main

import com.google.firebase.firestore.*
import ru.ic218.mychatapp.domain.interfaces.MessageRepository
import ru.ic218.mychatapp.domain.interfaces.FirebaseRepository
import ru.ic218.mychatapp.domain.model.PrefRepository
import java.util.concurrent.Executors

class MainInteractor(
    private val firebaseRepository: FirebaseRepository,
    private val messageRepository: MessageRepository,
    private val prefRepository: PrefRepository
) {

    private var firebaseListeners: MutableList<ListenerRegistration> = mutableListOf()

    fun subscribeFirebase(accountCallback: (Int) -> Unit) {
        if (prefRepository.isCompleteInstall) {
            firebaseListeners.add(
                firebaseRepository.getMessageReference()
                    .addSnapshotListener(Executors.newSingleThreadExecutor(),
                        object : EventListener<QuerySnapshot> {
                            override fun onEvent(value: QuerySnapshot?, ex: FirebaseFirestoreException?) {
                                value?.let { messageRepository.insertMessagesToDb(it) }
                            }
                        })
            )

            firebaseListeners.add(
                firebaseRepository.getAccountReference()
                    .addSnapshotListener(Executors.newSingleThreadExecutor(),
                        object : EventListener<QuerySnapshot> {
                            override fun onEvent(value: QuerySnapshot?, ex: FirebaseFirestoreException?) {
                                value?.let { accountCallback.invoke(it.size()) }
                            }
                        })
            )



            firebaseRepository.getAccountReference().document(prefRepository.ownerId.toString())
                .set(Pair(prefRepository.ownerId, "online"))
        }
    }

    fun unSubscribeFirebase() {
        firebaseRepository.getAccountReference().document(prefRepository.ownerId.toString()).delete()
        firebaseListeners.forEach { it.remove() }
    }

    fun updateNameToFirebase(name: String) {
        firebaseRepository.getMessageReference()
            .whereEqualTo("ownerId", prefRepository.ownerId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.forEach {
                        firebaseRepository.getMessageReference().document(it.id)
                            .update(mapOf(Pair("ownerName", name)))
                    }
                }
            }
    }

    fun getMessagesLiveData() = messageRepository.getMessagesLiveData()

    fun sendMessage(messageText: String) = messageRepository.sendMessage(messageText)
}