package org.mesonet.app.widget.dependencyinjection;

import org.mesonet.app.WidgetProvider;
import org.mesonet.app.widget.WidgetProviderLarge;
import org.mesonet.core.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = WidgetProviderLargeModule.class)
public interface WidgetProviderLargeSubcomponent extends AndroidInjector<WidgetProviderLarge>
{

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<WidgetProviderLarge> {
    }
}
