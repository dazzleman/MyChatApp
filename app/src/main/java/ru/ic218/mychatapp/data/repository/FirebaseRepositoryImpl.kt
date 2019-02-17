package ru.ic218.mychatapp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.ic218.mychatapp.domain.interfaces.FirebaseRepository

class FirebaseRepositoryImpl(private val db: FirebaseFirestore): FirebaseRepository {

    override fun getMessageReference(): CollectionReference {
        return db.collection("chat")
    }

    override fun getAccountReference(): CollectionReference {
        return db.collection("account")
    }
}