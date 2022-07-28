package com.respire.startapp.di

import com.respire.startapp.repositories.EntityFlowRepository
import com.respire.startapp.repositories.EntityFlowRepositoryImpl
import com.respire.startapp.repositories.EntityRepository
import com.respire.startapp.repositories.EntityRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class ImplementationsModule {

    @Binds
    internal abstract fun bindEntityRepository(entityRepository: EntityRepositoryImpl): EntityRepository

    @Binds
    internal abstract fun bindEntityFlowRepository(entityRepository: EntityFlowRepositoryImpl): EntityFlowRepository

}
