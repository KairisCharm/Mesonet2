package org.mesonet.app.maps.dependencyinjection;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.mesonet.app.MesonetApplication;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.filterlist.dependencyinjection.FilterListFragmentSubcomponent;
import org.mesonet.app.maps.MapListFragment;
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


@Module
abstract class MapListFragmentModule
{
    @Binds
    @PerFragment
    abstract Fragment Fragment(MapListFragment inMapsListFragment);
}
