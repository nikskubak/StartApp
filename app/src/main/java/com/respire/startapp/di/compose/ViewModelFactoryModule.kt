package com.respire.startapp.di.compose

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.respire.startapp.domain.repo.ModelRepository
import com.respire.startapp.uiCompose.MainComposeViewModel
import com.respire.startapp.uiCompose.MainComposeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelFactoryModule {

    @Provides
    fun provideMainComposeViewModelFactory(
        app: Application,
        modelRepository: ModelRepository
    ): MainComposeViewModelFactory {
        return object : MainComposeViewModelFactory {
            override fun create(handle: SavedStateHandle): MainComposeViewModel {
                return MainComposeViewModel(
                    app,
                    modelRepository,
                    handle
                )
            }
        }
    }
}