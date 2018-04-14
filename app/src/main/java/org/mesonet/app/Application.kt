package org.mesonet.app

import android.app.Activity

import org.mesonet.app.dependencyinjection.DaggerApplicationComponent

import javax.inject.Inject
import javax.inject.Singleton

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector

@Singleton
class Application @Inject
constructor() : android.app.Application(), HasActivityInjector {
    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.create().Inject(this)
    }


    override fun activityInjector(): AndroidInjector<Activity>? {
        return mActivityInjector
    }
}
