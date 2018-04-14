package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.content.Context;

import org.mesonet.app.Application;
import org.mesonet.app.MainActivity;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = AndroidInjectionModule.class,
        subcomponents = {MainActivitySubcomponent.class})
abstract class ApplicationModule
{
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    MainActivityInjectorFactory(MainActivitySubcomponent.Builder inBuilder);


    @Provides
    @Singleton
    static Context Context(Application inApp)
    {
        return inApp;
    }
}