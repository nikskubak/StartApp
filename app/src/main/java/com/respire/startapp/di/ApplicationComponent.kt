package com.respire.startapp.di

import com.respire.startapp.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ContributorsModule::class,
        ImplementationsModule::class,
        ContextModule::class]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>() {
        abstract fun contextModule(contextModule: ContextModule): Builder
        abstract fun dataModule(dataModule: DataModule): Builder
    }
}
