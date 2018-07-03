package org.mesonet.app.widget.dependencyinjection;

import org.mesonet.app.widget.WidgetProvider;
import org.mesonet.core.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@PerActivity
@Subcomponent(modules = WidgetProviderModule.class)
public interface WidgetProviderSubcomponent extends AndroidInjector<WidgetProvider>
{

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<WidgetProvider> {
    }
}
