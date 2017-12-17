package org.mesonet.app.dependencyinjection;

import android.app.Activity;

import org.mesonet.app.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = AndroidInjectionModule.class,
        subcomponents = {MainActivitySubcomponent.class})
abstract class MesonetApplicationModule
{
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    MainActivityInjectorFactory(MainActivitySubcomponent.Builder inBuilder);
}