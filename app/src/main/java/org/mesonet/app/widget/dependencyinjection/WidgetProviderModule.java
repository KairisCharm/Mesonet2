package org.mesonet.app.widget.dependencyinjection;


import android.app.Activity;
import android.content.Context;

import org.mesonet.androidsystem.DeviceLocation;
import org.mesonet.app.Application;
import org.mesonet.app.WidgetProvider;
import org.mesonet.app.widget.EmptyLocationProvider;
import org.mesonet.core.PerActivity;
import org.mesonet.core.PerChildFragment;
import org.mesonet.dataprocessing.LocationProvider;
import org.mesonet.dataprocessing.site.mesonetdata.MesonetDataController;
import org.mesonet.dataprocessing.userdata.Preferences;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = AndroidSupportInjectionModule.class)
public abstract class WidgetProviderModule
{
    @Provides
    @PerActivity
    static LocationProvider LocationProvider(EmptyLocationProvider inEmptyLocationProvider)
    {
        return inEmptyLocationProvider;
    }
}
