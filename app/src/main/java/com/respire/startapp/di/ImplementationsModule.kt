package com.respire.startapp.di

import com.respire.startapp.repositories.EntityFlowRepository
import com.respire.startapp.repositories.EntityFlowRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class ImplementationsModule {

    @Binds
    internal abstract fun bindEntityFlowRepository(entityRepository: EntityFlowRepositoryImpl): EntityFlowRepository

}
