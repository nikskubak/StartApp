package com.respire.startapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.respire.startapp.base.Result
import com.respire.startapp.database.AppDatabase
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityFlowRepositoryImpl @Inject constructor(
    var network: NetworkService,
    var database: AppDatabase
) : EntityFlowRepository {

    var availableEntities: List<Entity>? = null

//    override fun getEntities(isConnected : Boolean): LiveData<Result<List<Entity>>> {
//        val resultLivaData = MutableLiveData<Result<List<Entity>>>()
//        val result = Result<List<Entity>>()
//        if (availableEntities == null) {
//            val job = CoroutineScope(Dispatchers.Main).launch {
//                try {
//                    val list = if(isConnected) retrieveEntitiesFromNetwork() else retrieveEntitiesFromDatabase()
//                    availableEntities = list
//                    result.data = availableEntities
//                } catch (e: Exception) {
//                    result.error = e
//                } finally {
//                    resultLivaData.value = result
//                }
//            }
//        } else {
//            result.data = availableEntities
//            resultLivaData.value = result
//        }
//        return resultLivaData
//    }

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
            Log.e("flow", "start flow")
            val entities = retrieveEntitiesFromNetwork()
            emit(Result<List<Entity>>().apply { data = entities })
            Log.e("flow", "finish flow")
        }
    }
}