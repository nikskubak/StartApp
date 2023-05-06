package com.respire.startapp.di.hilt

import com.respire.startapp.domain.repo.ModelRepository
import com.respire.startapp.data.repositories.ModelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class HiltImplementationsModule {

    @Binds
    internal abstract fun bindModelRepository(modelRepository: ModelRepositoryImpl): ModelRepository
}