package com.respire.startapp.uiComposeHilt

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
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
    var app : Application
) : AndroidViewModel(app) {

    private var _baseUiState = MutableStateFlow(BaseUiState())
    var baseUiState: StateFlow<BaseUiState> = _baseUiState

    var errorUiState = MutableStateFlow<String?>(null)
    private var _modelsUiState = MutableStateFlow(Result.success(emptyList<Model>()))
    var modelsUiState: StateFlow<Result<List<Model>>> = _modelsUiState.asStateFlow()

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
                _baseUiState.update { BaseUiState(false) }
            }
            .catch { exception ->
                exception.printStackTrace()
                errorUiState.value = exception.message
            }
            .launchIn(viewModelScope)
    }

     fun openAppInGooglePlay(it: String?) {
        try {
            app.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$it")
                ).apply {
                    addFlags(FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (anfe: ActivityNotFoundException) {
            app.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$it")
                ).apply {
                    addFlags(FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

}