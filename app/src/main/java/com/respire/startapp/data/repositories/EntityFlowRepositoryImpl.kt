package com.respire.startapp.data.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.respire.startapp.base.Result
import com.respire.startapp.data.database.AppDatabase
import com.respire.startapp.data.database.Entity
import com.respire.startapp.data.network.NetworkService
import com.respire.startapp.domain.repo.EntityFlowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityFlowRepositoryImpl @Inject constructor(
    var network: NetworkService,
    var database: AppDatabase
) : EntityFlowRepository {

    private suspend fun retrieveEntitiesFromNetwork(): List<Entity>? {
        return withContext(Dispatchers.IO) {
            val response = network.getEntities.execute()
            val list = if (response.isSuccessful) response.body()?.record else mutableListOf()
            saveEntitiesToDatabase(list)
            list
        }
    }

    private suspend fun retrieveEntitiesFromDatabase(): List<Entity>? {
        return withContext(Dispatchers.IO) {
            database.getEntityDao().getAll()
        }
    }

    private fun retrieveEntitiesFromFirestore(): Flow<List<Entity>> {
        return callbackFlow {
            var entities = mutableListOf<Entity>()
            FirebaseFirestore.getInstance().collection("apps")
                .get()
                .addOnSuccessListener { data ->
                    data.documents.forEach { doc ->
                        doc.toObject(Entity::class.java)?.let {
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

    private suspend fun saveEntitiesToDatabase(entities: List<Entity>?) {
        withContext(Dispatchers.IO) {
            database.getEntityDao().insertAll(entities)
        }
    }

    override fun getEntities(isConnected: Boolean): Flow<Result<List<Entity>>> {
        return if (isConnected) retrieveEntitiesFromFirestore()
            .onEach { saveEntitiesToDatabase(it) }
            .map {
                Result<List<Entity>>().apply {
                    data = it
                }
            }
            .flowOn(Dispatchers.IO) else flow {
            emit(Result<List<Entity>>().apply { data = retrieveEntitiesFromDatabase() })
        }
    }
}