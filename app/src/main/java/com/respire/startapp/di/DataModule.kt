package com.respire.startapp.di

import android.content.Context
import com.respire.startapp.database.AppDatabase
import com.respire.startapp.network.NetworkService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DataModule(context: Context) {

    private var database: AppDatabase? = null

    init {
        database = AppDatabase.getAppDataBase(context)
    }

    @Provides
    @Singleton
    open fun provideNetworkService(): NetworkService {
        return NetworkService.getAuthRetrofitService("https://api.myjson.com/bins/cqx93/")!!
    }

    @Singleton
    @Provides
    open fun providesDatabase(): AppDatabase {
        return database!!
    }
}
