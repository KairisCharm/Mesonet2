package org.mesonet.app.dependencyinjection;

import android.content.Context;

import org.mesonet.app.Application;
import org.mesonet.app.widget.dependencyinjection.WidgetProviderLargeSubcomponent;
import org.mesonet.app.widget.dependencyinjection.WidgetProviderSubcomponent;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {WidgetProviderSubcomponent.class,
                         WidgetProviderLargeSubcomponent.class})
public class ApplicationContextModule
{
    private Context sContext;

    public ApplicationContextModule(Application inApplication)
    {
        sContext = inApplication;
    }



    @Provides
    public Context Context()
    {
        return sContext;
    }
}

