package com.respire.startapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.respire.startapp.base.Result
import com.respire.startapp.data.database.Entity
import com.respire.startapp.data.network.NetworkUtil
import com.respire.startapp.domain.model.AccountEntity
import com.respire.startapp.domain.repo.AccountRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LoginViewModel(
    val app: Application,
    var accountRepository: AccountRepository
) : AndroidViewModel(app) {

    var errorUiState = MutableStateFlow<String?>(null)
    private var _entitiesUiState = MutableStateFlow(Result<AccountEntity>())
    var entitiesUiState: StateFlow<Result<AccountEntity>> = _entitiesUiState

    fun getEntities() {
        if (_entitiesUiState.value.data == null) {
            login()
        }
    }

    fun login() {
        accountRepository.login()
            .onEach { _entitiesUiState.value = it }
            .catch { exception ->
                exception.printStackTrace()
                errorUiState.value = exception.message
            }
            .launchIn(viewModelScope)
    }

    class Factory @Inject constructor(
        var application: Application,
        var accountRepository: AccountRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(application, accountRepository) as T
        }
    }

}