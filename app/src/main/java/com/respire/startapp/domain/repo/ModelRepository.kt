package com.respire.startapp.domain.repo

import com.respire.startapp.domain.models.Model
import kotlinx.coroutines.flow.Flow

interface ModelRepository {
    fun getModels(isConnected : Boolean) : Flow<Result<List<Model>>>
}