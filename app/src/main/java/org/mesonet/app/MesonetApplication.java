package org.mesonet.app;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import org.mesonet.app.androidsystem.UserSettings;
import org.mesonet.app.dependencyinjection.DaggerMesonetApplicationComponent;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

@Singleton
public class MesonetApplication extends Application implements HasActivityInjector
{
    @Inject
    DispatchingAndroidInjector<Activity> mActivityInjector;



    @Inject
    public MesonetApplication(){}



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
