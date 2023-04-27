package com.respire.startapp.di

import com.respire.startapp.domain.repo.ModelRepository
import com.respire.startapp.data.repositories.ModelRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class ImplementationsModule {

    @Binds
    internal abstract fun bindModelRepository(modelRepository: ModelRepositoryImpl): ModelRepository
}
