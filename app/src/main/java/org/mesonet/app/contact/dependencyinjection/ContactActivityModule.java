package org.mesonet.app.contact.dependencyinjection;

import android.app.Activity;
import android.content.Context;

import org.mesonet.androidsystem.DeviceLocation;
import org.mesonet.androidsystem.UserSettings;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.contact.ContactActivity;
import org.mesonet.core.PerActivity;
import org.mesonet.dataprocessing.LocationProvider;
import org.mesonet.dataprocessing.userdata.Preferences;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = AndroidSupportInjectionModule.class)
public abstract class ContactActivityModule
{
    @Binds
    @PerActivity
    abstract Activity Activity(ContactActivity inContactActivity);

    @Binds
    @PerActivity
    abstract BaseActivity BaseActivity(ContactActivity inContactActivity);

    @Binds
    @PerActivity
    abstract LocationProvider LocationProvider(DeviceLocation inDeviceLocation);
}
