package com.respire.startapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.respire.startapp.base.Result
import com.respire.startapp.database.AppDatabase
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityRepositoryImpl @Inject constructor(
    var network: NetworkService,
    var database: AppDatabase
) : EntityRepository {

    var availableEntities: MutableList<Entity>? = null

    override fun getEntities(isConnected : Boolean): LiveData<Result<MutableList<Entity>>> {
        val resultLivaData = MutableLiveData<Result<MutableList<Entity>>>()
        val result = Result<MutableList<Entity>>()
        if (availableEntities == null) {
            val job = CoroutineScope(Dispatchers.Main).launch {
                try {
                    val list = if(isConnected) retrieveEntitiesFromNetwork() else retrieveEntitiesFromDatabase()
                    availableEntities = list
                    result.data = availableEntities
                } catch (e: Exception) {
                    result.error = e
                } finally {
                    resultLivaData.value = result
                }
            }
        } else {
            result.data = availableEntities
            resultLivaData.value = result
        }
        return resultLivaData
    }

    private suspend fun retrieveEntitiesFromNetwork(): MutableList<Entity>? {
        return withContext(Dispatchers.IO) {
            val response = network.getEntities.execute()
            val list = if (response.isSuccessful) response.body() else mutableListOf()
            saveEntitiesToDatabase(list)
            list
        }
    }

    private suspend fun retrieveEntitiesFromDatabase(): MutableList<Entity>? {
        return withContext(Dispatchers.IO) {
            database.getEntityDao().getAll()
        }
    }

    private suspend fun saveEntitiesToDatabase(entities: MutableList<Entity>?) {
        withContext(Dispatchers.IO) {
            database.getEntityDao().insertAll(entities)
        }
    }
}