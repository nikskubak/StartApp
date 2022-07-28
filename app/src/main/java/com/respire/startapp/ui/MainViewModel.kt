package com.respire.startapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.respire.startapp.base.ObservableAndroidViewModel
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkUtil
import com.respire.startapp.repositories.EntityFlowRepository
import com.respire.startapp.repositories.EntityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
    val app: Application,
    var entityRepository: EntityRepository,
    var entityFlowRepository: EntityFlowRepository
) : ObservableAndroidViewModel(app) {

    //LiveData for UI
    val entitiesLiveData = MutableLiveData<Result<List<Entity>>>()
    val errorMessageLiveData = MutableLiveData<String>()

    //StateFlow for UI
    private var _entitiesUiState = MutableStateFlow(Result<List<Entity>>())
    var entitiesUiState: StateFlow<Result<List<Entity>>> = _entitiesUiState

    fun getEntities(): LiveData<Result<List<Entity>>> {
        return entityRepository.getEntities(NetworkUtil.isConnected(app.baseContext))
    }

    fun getFlowEntities() {
        viewModelScope.launch {
            entityFlowRepository.getEntities(NetworkUtil.isConnected(app.baseContext))
                .catch { exception -> errorMessageLiveData.value = exception.message }
                .collect {
                    _entitiesUiState.value = it
                }
        }
    }

    class Factory @Inject constructor(
        var application: Application,
        var entityRepository: EntityRepository,
        var entityFlowRepository: EntityFlowRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application, entityRepository, entityFlowRepository) as T
        }
    }

}