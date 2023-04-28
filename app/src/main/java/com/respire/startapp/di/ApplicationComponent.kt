package com.respire.startapp.di

import com.respire.startapp.App
import com.respire.startapp.di.compose.ViewModelFactoryModule
import com.respire.startapp.di.compose.ViewModelModule
import com.respire.startapp.uiCompose.MainComposeViewModelFactory
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [DataModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ContributorsModule::class,
        ImplementationsModule::class,
        ContextModule::class

        ,
        ViewModelModule::class,
        ViewModelFactoryModule::class]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        fun contextModule(contextModule: ContextModule): Builder
        fun dataModule(dataModule: DataModule): Builder
        fun build(): ApplicationComponent
    }

    fun inject(app: App)

    fun getMainComposeViewModelFactory(): MainComposeViewModelFactory

}
