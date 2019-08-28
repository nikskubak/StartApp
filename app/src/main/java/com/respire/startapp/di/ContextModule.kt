package com.respire.startapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class ContextModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return application.baseContext
    }

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }
}
