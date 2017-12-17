package org.mesonet.app;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import org.mesonet.app.dependencyinjection.DaggerMesonetApplicationComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class MesonetApplication extends Application implements HasActivityInjector
{
    @Inject
    DispatchingAndroidInjector<Activity> mActivityInjector;



    @Override
    public void onCreate()
    {
        super.onCreate();
        DaggerMesonetApplicationComponent.create().Inject(this);
    }



    @Override
    public AndroidInjector<Activity> activityInjector()
    {
        return mActivityInjector;
    }
}
