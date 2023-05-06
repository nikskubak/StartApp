package com.respire.startapp

import android.app.Application
import com.respire.startapp.di.ApplicationComponent
import com.respire.startapp.di.ContextModule
import com.respire.startapp.di.DaggerApplicationComponent
import com.respire.startapp.di.DataModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    companion object{
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .dataModule(DataModule(this))
            .build()
        component.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}