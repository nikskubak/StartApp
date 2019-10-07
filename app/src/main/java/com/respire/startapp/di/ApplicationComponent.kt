package com.respire.startapp.di

import com.respire.startapp.App
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
        ContextModule::class]
)
interface ApplicationComponent {

//    @Component.Builder
//    abstract class Builder : AndroidInjector.Builder<App>() {
//        abstract fun contextModule(contextModule: ContextModule): Builder
//        abstract fun dataModule(dataModule: DataModule): Builder
//    }

    @Component.Builder
    interface Builder {

//        @BindsInstance
//        fun application(app: App): Builder
        fun contextModule(contextModule: ContextModule): Builder
        fun dataModule(dataModule: DataModule): Builder
        fun build(): ApplicationComponent
    }

    fun inject(app: App)
//    fun inject(app: EntityRepositoryTest)

}
