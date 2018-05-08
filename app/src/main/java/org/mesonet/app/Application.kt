package org.mesonet.app

import android.app.Activity

import org.mesonet.app.dependencyinjection.DaggerApplicationComponent

import javax.inject.Inject
import javax.inject.Singleton

import android.content.BroadcastReceiver
import dagger.android.*
import org.mesonet.app.dependencyinjection.ApplicationContextModule


@Singleton
class Application @Inject
constructor() : android.app.Application(), HasActivityInjector, HasBroadcastReceiverInjector {
    @Inject
    internal lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var mBroadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>


    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder().applicationContextModule(ApplicationContextModule(this)).build().Inject(this)
    }


    override fun activityInjector(): AndroidInjector<Activity>? {
        return mActivityInjector
    }


    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> {
        return mBroadcastReceiverInjector
    }
}
