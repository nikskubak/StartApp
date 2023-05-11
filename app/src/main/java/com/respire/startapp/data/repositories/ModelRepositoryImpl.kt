package com.respire.startapp.data.repositories

import com.respire.startapp.data.sources.database.AppDatabase
import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.data.sources.database.models.mapToDomainModel
import com.respire.startapp.data.sources.firestore.FirestoreManager
import com.respire.startapp.data.sources.firestore.models.mapToDbModel
import com.respire.startapp.data.sources.firestore.models.mapToDomainModel
import com.respire.startapp.data.sources.network.NetworkService
import com.respire.startapp.data.sources.network.models.ApiSubModel
import com.respire.startapp.data.sources.network.models.mapToDbModel
import com.respire.startapp.domain.models.Model
import com.respire.startapp.domain.repo.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModelRepositoryImpl @Inject constructor(
    var network: NetworkService,
    var database: AppDatabase,
    var firestoreManager: FirestoreManager
) : ModelRepository {

    private suspend fun retrieveModelsFromNetwork(): List<ApiSubModel>? {
        return withContext(Dispatchers.IO) {
            val response = network.getEntities()
            val list = if (response.isSuccessful) response.body()?.record else mutableListOf()
            saveModelsToDatabase(list?.map { it.mapToDbModel() })
            list
        }
    }

    private suspend fun retrieveModelsFromDatabase(): List<DbModel>? {
        return withContext(Dispatchers.IO) {
            database.getDbModelDao().getAll()
        }
    }

    private suspend fun saveModelsToDatabase(entities: List<DbModel>?) {
        withContext(Dispatchers.IO) {
            database.getDbModelDao().insertAll(entities)
        }
    }

    override fun getModels(isConnected: Boolean): Flow<Result<List<Model>>> {
        return if (isConnected) firestoreManager.getData()
            .onEach { firestoreModels ->
                saveModelsToDatabase(firestoreModels.map { it.mapToDbModel() })
            }
            .map { firestoreModels ->
                Result.success(firestoreModels.map { it.mapToDomainModel() })
            }
            .flowOn(Dispatchers.IO) else flow {
            emit(
                Result.success(
                    retrieveModelsFromDatabase().orEmpty().map { it.mapToDomainModel() })
            )
        }
    }

    override fun getModel(id: String): Flow<Result<Model>> {
        return database.getDbModelDao().get(id)
            .map {
                Result.success(it.mapToDomainModel())
            }.flowOn(Dispatchers.IO)
    }
}