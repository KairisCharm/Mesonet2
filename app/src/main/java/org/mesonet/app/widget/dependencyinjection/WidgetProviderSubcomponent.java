package org.mesonet.app.widget.dependencyinjection;

import android.content.Context;

import org.mesonet.app.WidgetProvider;
import org.mesonet.app.usersettings.UserSettingsActivity;
import org.mesonet.core.PerActivity;

import javax.inject.Singleton;

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
