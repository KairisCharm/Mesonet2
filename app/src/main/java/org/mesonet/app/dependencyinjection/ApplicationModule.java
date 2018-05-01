package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.content.Context;

import org.mesonet.app.Application;
import org.mesonet.app.MainActivity;
import org.mesonet.app.about.ContactActivity;
import org.mesonet.app.usersettings.UserSettingsActivity;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.IntoMap;

@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = {MainActivitySubcomponent.class,
                         UserSettingsActivitySubcomponent.class,
                         ContactActivitySubcomponent.class})
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
}