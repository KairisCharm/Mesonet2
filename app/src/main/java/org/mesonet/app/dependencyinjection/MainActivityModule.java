package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.app.Fragment;

import org.mesonet.app.MainActivity;
import org.mesonet.app.mesonetdata.dependencyinjection.MesonetFragmentSubcomponent;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.site.dependencyinjection.SiteOverviewFragmentSubcomponent;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.FragmentKey;
import dagger.multibindings.IntoMap;



@Module(subcomponents = {SiteOverviewFragmentSubcomponent.class})
public abstract class MainActivityModule
{
    @Binds
    @IntoMap
    @FragmentKey(SiteOverviewFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    SiteOverviewFragmentInjectorFactory(SiteOverviewFragmentSubcomponent.Builder inBuilder);

    @Binds
    @PerActivity
    abstract Activity Activity(MainActivity inMainActivity);

    @Binds
    @PerActivity
    abstract Preferences Preferences(MainActivity inMainActivity);

    @Provides
    @PerActivity
    static PreferencesObservable PreferencesObservable(MainActivity inMainActivity)
    {
        return inMainActivity.GetPreferencesObservable();
    }
}