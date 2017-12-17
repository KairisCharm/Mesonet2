package org.mesonet.app.dependencyinjection;

import android.app.Fragment;

import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.site.SiteOverviewFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.FragmentKey;
import dagger.multibindings.IntoMap;



@Module(subcomponents = FilterListFragmentSubcomponent.class)
abstract class SiteOverviewFragmentModule {

    // TODO (ContributesAndroidInjector) remove this in favor of @ContributesAndroidInjector
    @Binds
    @IntoMap
    @FragmentKey(FilterListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    filterListFragmentInjectorFactory(FilterListFragmentSubcomponent.Builder builder);

    @Binds
    @Named("SiteOverviewFragment")
    @PerFragment
    abstract Fragment fragment(SiteOverviewFragment inSiteOverviewFragment);

    @Binds
    @PerFragment
    abstract FilterListFragment.FilterListCloser FilterListCloser(SiteOverviewFragment inSiteOverviewFragment);
}
