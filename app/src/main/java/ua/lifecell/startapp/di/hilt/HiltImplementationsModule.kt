package ua.lifecell.startapp.di.hilt

import ua.lifecell.startapp.data.repositories.ModelRepositoryImpl
import ua.lifecell.startapp.domain.repo.ModelRepository
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
