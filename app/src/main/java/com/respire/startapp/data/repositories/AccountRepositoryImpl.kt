package com.respire.startapp.data.repositories

import com.respire.startapp.data.sources.database.AppDatabase
import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.data.sources.network.NetworkService
import com.respire.startapp.domain.models.AccountEntity
import com.respire.startapp.domain.repo.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.respire.startapp.base.Result

class AccountRepositoryImpl @Inject constructor(
    var network: NetworkService,
    var database: AppDatabase
) : AccountRepository {

    private suspend fun retrieveEntitiesFromNetwork(): AccountEntity? {
        return withContext(Dispatchers.IO) {
//            val response = network.getEntities.execute()
//            val list = if (response.isSuccessful) response.body()?.record else mutableListOf()
//            saveEntitiesToDatabase(list)
//            list
            AccountEntity("accessToken")
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

    override fun login(): Flow<Result<AccountEntity>> {
        return flow {
            emit(Result<AccountEntity>().apply { data = retrieveEntitiesFromNetwork() })
        }
    }
}