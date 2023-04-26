package com.respire.startapp.data.repositories

import com.respire.startapp.data.sources.database.AppDatabase
import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.data.sources.database.models.mapToDomainModel
import com.respire.startapp.data.sources.firestore.FirestoreManager
import com.respire.startapp.data.sources.firestore.models.mapToDbModel
import com.respire.startapp.data.sources.firestore.models.mapToDomainModel
import com.respire.startapp.data.sources.network.NetworkService
import com.respire.startapp.data.sources.network.models.SubResponseModel
import com.respire.startapp.data.sources.network.models.mapToDbModel
import com.respire.startapp.domain.models.DomainModel
import com.respire.startapp.domain.repo.EntityFlowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityFlowRepositoryImpl @Inject constructor(
    var network: NetworkService,
    var database: AppDatabase,
    var firestoreManager: FirestoreManager
) : EntityFlowRepository {

    private suspend fun retrieveEntitiesFromNetwork(): List<SubResponseModel>? {
        return withContext(Dispatchers.IO) {
            val response = network.getEntities()
            val list = if (response.isSuccessful) response.body()?.record else mutableListOf()
            saveEntitiesToDatabase(list?.map { it.mapToDbModel() })
            list
        }
    }

    private suspend fun retrieveEntitiesFromDatabase(): List<DbModel>? {
        return withContext(Dispatchers.IO) {
            database.getDbModelDao().getAll()
        }
    }

    private suspend fun saveEntitiesToDatabase(entities: List<DbModel>?) {
        withContext(Dispatchers.IO) {
            database.getDbModelDao().insertAll(entities)
        }
    }

    override fun getEntities(isConnected: Boolean): Flow<Result<List<DomainModel>>> {
        return if (isConnected) firestoreManager.getData()
            .onEach { firestoreModels ->
                saveEntitiesToDatabase(firestoreModels.map { it.mapToDbModel() })
            }
            .map { firestoreModels ->
                Result.success(firestoreModels.map { it.mapToDomainModel() })
            }
            .flowOn(Dispatchers.IO) else flow {
            emit(
                Result.success(
                    retrieveEntitiesFromDatabase().orEmpty().map { it.mapToDomainModel() })
            )
        }
    }
}