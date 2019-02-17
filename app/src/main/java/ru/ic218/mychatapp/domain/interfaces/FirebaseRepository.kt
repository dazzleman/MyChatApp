package ru.ic218.mychatapp.domain.interfaces

import com.google.firebase.firestore.CollectionReference

interface FirebaseRepository {

    fun getMessageReference(): CollectionReference
    fun getAccountReference(): CollectionReference
}