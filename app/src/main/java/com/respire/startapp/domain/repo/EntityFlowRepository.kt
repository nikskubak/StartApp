package com.respire.startapp.domain.repo

import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.domain.models.DomainModel
import kotlinx.coroutines.flow.Flow

interface EntityFlowRepository {
    fun getEntities(isConnected : Boolean) : Flow<Result<List<DomainModel>>>
}