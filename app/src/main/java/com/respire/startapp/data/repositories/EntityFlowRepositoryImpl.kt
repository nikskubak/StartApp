package com.respire.startapp.data.repositories

import com.respire.startapp.base.Result
import com.respire.startapp.data.database.AppDatabase
import com.respire.startapp.data.database.Entity
import com.respire.startapp.data.network.NetworkService
import com.respire.startapp.domain.repo.EntityFlowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    private suspend fun saveEntitiesToDatabase(entities: List<Entity>?) {
        withContext(Dispatchers.IO) {
            database.getEntityDao().insertAll(entities)
        }
    }

    override fun getEntities(isConnected: Boolean): Flow<Result<List<Entity>>> {
        return flow {
            emit(Result<List<Entity>>().apply { data = retrieveEntitiesFromNetwork() })
        }
    }
}