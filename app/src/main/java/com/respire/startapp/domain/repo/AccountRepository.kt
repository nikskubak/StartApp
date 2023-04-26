package com.respire.startapp.domain.repo

import com.respire.startapp.base.Result
import com.respire.startapp.domain.models.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun login() : Flow<Result<AccountEntity>>
}