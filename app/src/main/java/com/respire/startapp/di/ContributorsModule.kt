package com.respire.startapp.di

import com.respire.startapp.ui.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ContributorsModule {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity
}
