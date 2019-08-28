package com.respire.startapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.respire.startapp.base.ObservableAndroidViewModel
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity
import com.respire.startapp.network.NetworkUtil
import com.respire.startapp.repositories.EntityRepository
import javax.inject.Inject

class MainViewModel constructor(val app: Application, var entityRepository: EntityRepository) : ObservableAndroidViewModel(app) {

    fun getEntities(): LiveData<Result<MutableList<Entity>>> {
        return entityRepository.getEntities(NetworkUtil.getInstance().isConnected(app.baseContext))
    }

    class Factory @Inject constructor(var application: Application, var entityRepository: EntityRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application, entityRepository) as T
        }
    }

}