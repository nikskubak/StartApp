package com.respire.startapp

import com.estimote.sdk.BeaconManager
import com.respire.startapp.di.ContextModule
import com.respire.startapp.di.DaggerApplicationComponent
import com.respire.startapp.di.DataModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class App : DaggerApplication() {

    private var beaconManager: BeaconManager? = null

    override fun onCreate() {
        super.onCreate()
//        beaconManager =  BeaconManager(applicationContext)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .dataModule(DataModule(this))
            .create(this)
    }
}