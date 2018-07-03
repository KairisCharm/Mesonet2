package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.content.BroadcastReceiver;

import org.mesonet.androidsystem.DeviceLocation;
import org.mesonet.androidsystem.UserSettings;
import org.mesonet.app.MainActivity;
import org.mesonet.app.widget.WidgetProvider;
import org.mesonet.app.contact.ContactActivity;
import org.mesonet.app.contact.dependencyinjection.ContactActivitySubcomponent;
import org.mesonet.app.usersettings.UserSettingsActivity;
import org.mesonet.app.usersettings.dependencyinjection.UserSettingsActivitySubcomponent;
import org.mesonet.app.widget.WidgetProviderLarge;
import org.mesonet.app.widget.dependencyinjection.WidgetProviderLargeSubcomponent;
import org.mesonet.app.widget.dependencyinjection.WidgetProviderSubcomponent;
import org.mesonet.dataprocessing.LocationProvider;
import org.mesonet.dataprocessing.userdata.Preferences;
import org.mesonet.models.radar.RadarDetailCreator;
import org.mesonet.models.radar.RadarDetailModelCreator;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.BroadcastReceiverKey;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.IntoMap;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = {MainActivitySubcomponent.class,
                         UserSettingsActivitySubcomponent.class,
                         ContactActivitySubcomponent.class,
                         WidgetProviderSubcomponent.class,
                         WidgetProviderLargeSubcomponent.class})
abstract class ApplicationModule
{
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    MainActivityInjectorFactory(MainActivitySubcomponent.Builder inBuilder);

    @Binds
    @IntoMap
    @ActivityKey(UserSettingsActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    UserSettingsInjectorFactory(UserSettingsActivitySubcomponent.Builder inBuilder);

    @Binds
    @IntoMap
    @ActivityKey(ContactActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    ContactInjectorFactory(ContactActivitySubcomponent.Builder inBuilder);


    @Binds
    @IntoMap
    @BroadcastReceiverKey(WidgetProvider.class)
    abstract AndroidInjector.Factory<? extends BroadcastReceiver>
    WidgetProviderInjectorFactory(WidgetProviderSubcomponent.Builder inBuilder);


    @Binds
    @IntoMap
    @BroadcastReceiverKey(WidgetProviderLarge.class)
    abstract AndroidInjector.Factory<? extends BroadcastReceiver>
    WidgetProviderLargeInjectorFactory(WidgetProviderLargeSubcomponent.Builder inBuilder);

    @Binds
    abstract RadarDetailCreator RadarDetailCreator(RadarDetailModelCreator inCreator);

    @Binds
    abstract Preferences Preferences(UserSettings inSettings);

    @Binds
    abstract LocationProvider LocationProvider(DeviceLocation inDeviceLocation);
}