package com.respire.startapp.di.hilt

import android.content.Context
import androidx.room.Room
import com.respire.startapp.data.sources.database.AppDatabase
import com.respire.startapp.data.sources.firestore.FirestoreManager
import com.respire.startapp.data.sources.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class HiltDataModule {

    @Provides
    @Singleton
    open fun provideNetworkService(): NetworkService {
        return NetworkService.getRetrofitService("https://api.jsonbin.io/v3/b/5e6a0c3831c25c2b0fd34054/")!!
    }

    @Singleton
    @Provides
    open fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, "app.db")
            .build()
    }

    @Provides
    @Singleton
    open fun provideFirestore(): FirestoreManager {
        return FirestoreManager()
    }
}
