package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.content.Context;

import org.mesonet.androidsystem.UserSettings;
import org.mesonet.app.MainActivity;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.usersettings.UserSettingsActivity;
import org.mesonet.core.PerActivity;
import org.mesonet.dataprocessing.userdata.Preferences;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = AndroidSupportInjectionModule.class)
public abstract class UserSettingsActivityModule
{
    @Binds
    @PerActivity
    abstract Activity Activity(UserSettingsActivity inUserSettingsActivity);

    @Binds
    @PerActivity
    abstract BaseActivity BaseActivity(UserSettingsActivity inUserSettingsActivity);

    @Binds
    @PerActivity
    abstract Context Context(Activity inActivity);

    @Provides
    @PerActivity
    static Preferences Preferences(UserSettings inSettings)
    {
        return inSettings;
    }
}
