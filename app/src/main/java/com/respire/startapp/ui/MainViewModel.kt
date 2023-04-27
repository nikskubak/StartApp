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
import java.text.RuleBasedCollator
import javax.inject.Inject

class MainViewModel(
    val app: Application,
    var modelRepository: ModelRepository
) : AndroidViewModel(app) {

    private var _baseUiState = MutableStateFlow(BaseUiState())
    var baseUiState: StateFlow<BaseUiState> = _baseUiState

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
            .take(1)
            .onEach { _modelsUiState.value = it }
            .onStart {
                _baseUiState.update { BaseUiState(true) }
            }
            .onCompletion {
                _baseUiState.update { BaseUiState(false)  }
            }
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