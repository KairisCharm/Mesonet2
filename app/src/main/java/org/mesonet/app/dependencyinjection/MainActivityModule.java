package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.mesonet.app.MainActivity;
import org.mesonet.app.advisories.AdvisoriesFragment;
import org.mesonet.app.advisories.dependencyinjection.AdvisoriesFragmentSubcomponent;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.maps.MapListFragment;
import org.mesonet.app.maps.dependencyinjection.MapListFragmentSubcomponent;
import org.mesonet.app.radar.RadarFragment;
import org.mesonet.app.radar.dependencyinjection.RadarDataControllerSubcomponent;
import org.mesonet.app.radar.dependencyinjection.RadarFragmentSubcomponent;
import org.mesonet.app.radar.dependencyinjection.RadarSiteDataProviderSubcomponent;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.site.dependencyinjection.SiteOverviewFragmentSubcomponent;
import org.mesonet.app.site.mesonetdata.dependencyinjection.MesonetSiteDataControllerSubcomponent;
import org.mesonet.app.userdata.Preferences;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;



@Module(subcomponents = {SiteOverviewFragmentSubcomponent.class,
        MesonetSiteDataControllerSubcomponent.class,
        MapListFragmentSubcomponent.class,
        RadarFragmentSubcomponent.class,
        RadarSiteDataProviderSubcomponent.class,
        RadarDataControllerSubcomponent.class,
        AdvisoriesFragmentSubcomponent.class})
public abstract class MainActivityModule
{
    @Binds
    @IntoMap
    @FragmentKey(SiteOverviewFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    SiteOverviewFragmentInjectorFactory(SiteOverviewFragmentSubcomponent.Builder inBuilder);

    @Binds
    @IntoMap
    @FragmentKey(MapListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    MapListFragmentInjectorFactory(MapListFragmentSubcomponent.Builder inBuilder);

    @Binds
    @IntoMap
    @FragmentKey(RadarFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    RadarFragmentInjectorFactory(RadarFragmentSubcomponent.Builder inBuilder);

    @Binds
    @IntoMap
    @FragmentKey(AdvisoriesFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    AdvisoriesFragmentInjectorFactory(AdvisoriesFragmentSubcomponent.Builder inBuilder);

    @Binds
    @PerActivity
    abstract Activity Activity(MainActivity inMainActivity);


    @Binds
    @PerActivity
    abstract BaseActivity BaseActivity(MainActivity inMainActivity);


    @Provides
    @PerActivity
    static Preferences Preferences(BaseActivity inActivity)
    {
        return inActivity;
    }
}