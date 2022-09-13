package com.respire.startapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkUtil
import com.respire.startapp.repositories.EntityFlowRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
    val app: Application,
    var entityFlowRepository: EntityFlowRepository
) : AndroidViewModel(app) {

    var errorUiState = MutableStateFlow<String?>(null)
    private var _entitiesUiState = MutableStateFlow(Result<List<Entity>>())
    var entitiesUiState: StateFlow<Result<List<Entity>>> = _entitiesUiState

    fun getEntities() {
        if (_entitiesUiState.value.data.isNullOrEmpty()) {
            refreshEntities()
        }
    }

    fun refreshEntities() {
        entityFlowRepository.getEntities(NetworkUtil.isConnected(app.baseContext))
            .onEach { _entitiesUiState.value = it }
            .catch { exception ->
                exception.printStackTrace()
                errorUiState.value = exception.message
            }
            .launchIn(viewModelScope)
    }

    class Factory @Inject constructor(
        var application: Application,
        var entityFlowRepository: EntityFlowRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application, entityFlowRepository) as T
        }
    }

}