package com.respire.startapp.domain.repo

import com.respire.startapp.base.Result
import com.respire.startapp.data.database.Entity
import kotlinx.coroutines.flow.Flow

interface EntityFlowRepository {
    fun getEntities(isConnected : Boolean) : Flow<Result<List<Entity>>>
}