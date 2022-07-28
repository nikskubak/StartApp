package com.respire.startapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.respire.startapp.base.ObservableAndroidViewModel
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkUtil
import com.respire.startapp.repositories.EntityFlowRepository
import com.respire.startapp.repositories.EntityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
    val app: Application,
    var entityRepository: EntityRepository,
    var entityFlowRepository: EntityFlowRepository
) : ObservableAndroidViewModel(app) {

    val entitiesLiveData = MutableLiveData<Result<List<Entity>>>()

    fun getEntities(): LiveData<Result<List<Entity>>> {
        return entityRepository.getEntities(NetworkUtil.isConnected(app.baseContext))
    }

    fun getFlowEntities() {
        viewModelScope.launch{
            entityFlowRepository.getEntities(NetworkUtil.isConnected(app.baseContext)).collect {
                entitiesLiveData.value = it
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