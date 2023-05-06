package com.respire.startapp.di.hilt

import android.content.Context
import com.respire.startapp.data.sources.database.AppDatabase
import com.respire.startapp.data.sources.firestore.FirestoreManager
import com.respire.startapp.data.sources.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
open class HiltDataModule(context: Context) {

    private var database: AppDatabase? = null

    init {
        database = AppDatabase.getAppDataBase(context)
    }

    @Provides
    @Singleton
    open fun provideNetworkService(): NetworkService {
        return NetworkService.getAuthRetrofitService("https://api.jsonbin.io/v3/b/5e6a0c3831c25c2b0fd34054/")!!
    }

    @Singleton
    @Provides
    open fun providesDatabase(): AppDatabase {
        return database!!
    }

    @Provides
    @Singleton
    open fun provideFirestore(): FirestoreManager {
        return FirestoreManager()
    }
}
