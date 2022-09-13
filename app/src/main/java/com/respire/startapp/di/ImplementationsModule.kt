package com.respire.startapp.di

import com.respire.startapp.data.repositories.AccountRepositoryImpl
import com.respire.startapp.domain.repo.EntityFlowRepository
import com.respire.startapp.data.repositories.EntityFlowRepositoryImpl
import com.respire.startapp.domain.repo.AccountRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class ImplementationsModule {

    @Binds
    internal abstract fun bindEntityFlowRepository(entityRepository: EntityFlowRepositoryImpl): EntityFlowRepository

    @Binds
    internal abstract fun bindAccountRepository(accountRepository: AccountRepositoryImpl): AccountRepository

}
