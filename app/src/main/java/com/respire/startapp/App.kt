package com.respire.startapp

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.respire.startapp.di.ContextModule
import com.respire.startapp.di.DaggerApplicationComponent
import com.respire.startapp.di.DataModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .dataModule(DataModule(this))
            .build()
            .inject(this)
    }
}