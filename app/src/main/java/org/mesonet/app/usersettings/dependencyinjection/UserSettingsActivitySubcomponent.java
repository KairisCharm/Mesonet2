package org.mesonet.app.usersettings.dependencyinjection;

import org.mesonet.app.dependencyinjection.BaseActivityModule;
import org.mesonet.app.usersettings.UserSettingsActivity;
import org.mesonet.core.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;



@PerActivity
@Subcomponent(modules = { UserSettingsActivityModule.class})
public interface UserSettingsActivitySubcomponent extends AndroidInjector<UserSettingsActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<UserSettingsActivity> {
    }
}
