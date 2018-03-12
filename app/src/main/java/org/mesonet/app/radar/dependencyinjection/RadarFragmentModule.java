package org.mesonet.app.radar.dependencyinjection;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.mesonet.app.MesonetApplication;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.filterlist.dependencyinjection.FilterListFragmentSubcomponent;
import org.mesonet.app.radar.RadarFragment;
import org.mesonet.app.radar.RadarSiteDataProvider;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.site.SiteSelectionInterfaces;
import org.mesonet.app.site.mesonetdata.MesonetSiteDataController;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;


@Module(subcomponents = FilterListFragmentSubcomponent.class)
abstract class RadarFragmentModule
{
    @Binds
    @PerFragment
    abstract Fragment Fragment(RadarFragment inRadarFragment);


    @Binds
    @IntoMap
    @FragmentKey(FilterListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    filterListFragmentInjectorFactory(FilterListFragmentSubcomponent.Builder builder);


    @Binds
    @PerFragment
    abstract FilterListFragment.FilterListCloser FilterListCloser(RadarFragment inRadarFragment);

    @Provides
    @PerFragment
    static FilterListFragment.FilterListDataProvider FilterListDataProvider(RadarSiteDataProvider inSiteDataProvider)
    {
        return inSiteDataProvider;
    }



    @Binds
    @PerFragment
    abstract SiteSelectionInterfaces.SelectSiteListener OnSelectedListener(RadarSiteDataProvider inSiteDataController);
}
