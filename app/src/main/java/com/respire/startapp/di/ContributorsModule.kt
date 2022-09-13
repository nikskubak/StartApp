package com.respire.startapp.di

import com.respire.startapp.ui.MainActivity
import com.respire.startapp.ui.login.LoginActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ContributorsModule {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributeLoginActivity(): LoginActivity
}
