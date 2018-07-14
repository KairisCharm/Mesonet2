package org.mesonet.app.site.dependencyinjection;

import android.support.v4.app.Fragment;

import dagger.Provides;
import dagger.android.support.FragmentKey;

import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.filterlist.dependencyinjection.FilterListFragmentSubcomponent;
import org.mesonet.dataprocessing.SelectSiteListener;
import org.mesonet.dataprocessing.site.MesonetSiteDataController;
import org.mesonet.app.site.mesonetdata.dependencyinjection.MesonetUIControllerSubcomponent;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.core.PerFragment;
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;

import dagger.multibindings.IntoMap;



@Module(subcomponents = {FilterListFragmentSubcomponent.class})
abstract class SiteOverviewFragmentModule {

    // TODO (ContributesAndroidInjector) remove this in favor of @ContributesAndroidInjector
    @Binds
    @IntoMap
    @FragmentKey(FilterListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    filterListFragmentInjectorFactory(FilterListFragmentSubcomponent.Builder builder);

    @Binds
    @PerFragment
    abstract Fragment fragment(SiteOverviewFragment inSiteOverviewFragment);

    @Binds
    @PerFragment
    abstract FilterListFragment.FilterListCloser FilterListCloser(SiteOverviewFragment inSiteOverviewFragment);

    @Binds
    @PerFragment
    abstract FilterListDataProvider FilterListDataProvider(MesonetSiteDataController inSiteDataController);

    @Binds
    @PerFragment
    abstract SelectSiteListener OnSelectedListener(MesonetSiteDataController inSiteDataController);
}