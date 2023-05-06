package com.respire.startapp.di.hilt

import com.respire.startapp.data.repositories.ModelRepositoryImpl
import com.respire.startapp.domain.repo.ModelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HiltImplementationsModule {

    @Binds
    abstract fun bindModelRepository(modelRepository: ModelRepositoryImpl): ModelRepository
}
