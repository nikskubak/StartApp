package com.respire.startapp.uiComposeHilt

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.respire.startapp.data.sources.network.NetworkUtil
import com.respire.startapp.domain.models.Model
import com.respire.startapp.domain.repo.ModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainComposeHiltViewModel @Inject constructor(
    var modelRepository: ModelRepository,
    var app: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    private var _baseUiState = MutableStateFlow(BaseUiState())
    var baseUiState: StateFlow<BaseUiState> = _baseUiState

    var errorUiState = MutableStateFlow<String?>(null)
    private var _modelsUiState = MutableStateFlow(emptyList<Model>())
    var modelsUiState: StateFlow<List<Model>> = _modelsUiState.asStateFlow()

    init {
        getModels()
    }

    private fun getModels() {
        if (modelsUiState.value.isEmpty()) {
            Log.e("getModels", "getModels")
            refreshModels()
        }
    }

    private fun refreshModels() {
        modelRepository.getModels(NetworkUtil.isConnected(app.baseContext))
            .take(1)
            .onEach { _modelsUiState.value = it.getOrNull().orEmpty() }
            .onStart {
                _baseUiState.update { BaseUiState(true) }
            }
            .onCompletion {
                _baseUiState.update { BaseUiState(false) }
            }
            .catch { exception ->
                exception.printStackTrace()
                errorUiState.value = exception.message
            }
            .launchIn(viewModelScope)
    }

}