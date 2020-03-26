package com.respire.startapp.di

import com.respire.startapp.repositories.EntityRepository
import com.respire.startapp.repositories.EntityRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class ImplementationsModule {

    @Binds
    internal abstract fun bindEntityRepository(entityRepository: EntityRepositoryImpl): EntityRepository

}
