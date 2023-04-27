package com.respire.startapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.respire.startapp.data.sources.network.NetworkUtil
import com.respire.startapp.domain.models.Model
import com.respire.startapp.domain.repo.ModelRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MainViewModel(
    val app: Application,
    var modelRepository: ModelRepository
) : AndroidViewModel(app) {

    var errorUiState = MutableStateFlow<String?>(null)
    private var _modelsUiState = MutableStateFlow(Result.success(emptyList<Model>()))
    var modelsUiState: StateFlow<Result<List<Model>>> = _modelsUiState

    fun getModels() {
        if (_modelsUiState.value.getOrNull().isNullOrEmpty()) {
            refreshModels()
        }
    }

    fun refreshModels() {
        modelRepository.getModels(NetworkUtil.isConnected(app.baseContext))
            .onEach { _modelsUiState.value = it }
            .catch { exception ->
                exception.printStackTrace()
                errorUiState.value = exception.message
            }
            .launchIn(viewModelScope)
    }

    class Factory @Inject constructor(
        var application: Application,
        var modelRepository: ModelRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application, modelRepository) as T
        }
    }

}