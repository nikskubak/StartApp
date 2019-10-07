package com.respire.startapp.di.test

import android.content.Context
import com.respire.startapp.database.AppDatabase
import com.respire.startapp.di.DataModule
import com.respire.startapp.network.NetworkService

public class DataModuleTest(val context: Context) : DataModule(context) {
    override fun provideNetworkService(): NetworkService {
        return NetworkService.getAuthRetrofitService("https://api.myjson.com/bins/cqx93/")!!
    }

    override fun providesDatabase(): AppDatabase {
        return AppDatabase.getAppDataBase(context)!!
    }
}