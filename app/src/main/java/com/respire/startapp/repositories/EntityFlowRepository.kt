package com.respire.startapp.repositories

import androidx.lifecycle.LiveData
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import kotlinx.coroutines.flow.Flow

interface EntityFlowRepository {
    fun getEntities(isConnected : Boolean) : Flow<Result<List<Entity>>>
}