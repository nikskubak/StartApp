package ua.lifecell.startapp.domain.repo

import ua.lifecell.startapp.domain.models.Model
import kotlinx.coroutines.flow.Flow

interface ModelRepository {
    fun getModels(isConnected : Boolean) : Flow<Result<List<Model>>>
    fun getModel(id: String) : Flow<Result<Model>>
}