package com.respire.startapp.uiComposeHilt.details

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.respire.startapp.domain.models.Model
import com.respire.startapp.domain.repo.ModelRepository
import com.respire.startapp.uiComposeHilt.BaseUiState
import com.respire.startapp.uiComposeHilt.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    var modelRepository: ModelRepository,
    var app: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    private val modelId: String = checkNotNull(savedStateHandle[Screen.DetailsScreen.ITEM_ID])

    private var _baseUiState = MutableStateFlow(BaseUiState())
    var baseUiState: StateFlow<BaseUiState> = _baseUiState

    private var _modelUiState = MutableStateFlow<Model?>(null)
    var modelUiState: StateFlow<Model?> = _modelUiState.asStateFlow()

    init {
        getSelectedModel()
    }

    private fun getSelectedModel() {
        viewModelScope.launch {
            modelRepository.getModel(modelId).collect{ result ->
                _modelUiState.update {
                    result.getOrNull()
                }
            }
        }
    }

    fun openAppInGooglePlay(it: String?) {
        try {
            app.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$it")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (anfe: ActivityNotFoundException) {
            app.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$it")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

}