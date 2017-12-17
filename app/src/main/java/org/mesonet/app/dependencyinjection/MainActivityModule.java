package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.app.Fragment;

import org.mesonet.app.MainActivity;
import org.mesonet.app.site.SiteOverviewFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.FragmentKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {SiteOverviewFragmentSubcomponent.class})
abstract class MainActivityModule
{
    // TODO (ContributesAndroidInjector) remove this in favor of @ContributesAndroidInjector
    @Binds
    @IntoMap
    @FragmentKey(SiteOverviewFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    SiteOverviewFragmentInjectorFactory(SiteOverviewFragmentSubcomponent.Builder inBuilder);

    @Binds
    @PerActivity
    abstract Activity Activity(MainActivity inMainActivity);
}