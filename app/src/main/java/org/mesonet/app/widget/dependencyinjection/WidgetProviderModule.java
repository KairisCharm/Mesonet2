package org.mesonet.app.widget.dependencyinjection;

import org.mesonet.app.widget.EmptyLocationProvider;
import org.mesonet.core.PerActivity;
import org.mesonet.dataprocessing.LocationProvider;

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
