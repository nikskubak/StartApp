package com.respire.startapp.data.sources.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.respire.startapp.data.sources.firestore.models.FirestoreModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreManager {
    fun getData(): Flow<List<FirestoreModel>> {
        return callbackFlow {
            var entities = mutableListOf<FirestoreModel>()
            FirebaseFirestore.getInstance().collection("apps")
                .get()
                .addOnSuccessListener { data ->
                    data.documents.forEach { doc ->
                        doc.toObject(FirestoreModel::class.java)?.let {
                            entities.add(it)
                        }
                    }
                    trySend(entities)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    close(it)
                }
            awaitClose {
                FirebaseFirestore.getInstance().clearPersistence()
            }
        }
    }
}