package org.mesonet.app.site.dependencyinjection;

import android.app.Fragment;
import android.location.Location;

import dagger.Provides;
import kotlin.Pair;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.filterlist.dependencyinjection.FilterListFragmentSubcomponent;
import org.mesonet.app.mesonetdata.BaseMesonetSiteDataController;
import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.mesonetdata.MesonetSiteDataController;
import org.mesonet.app.mesonetdata.dependencyinjection.MesonetFragmentSubcomponent;
import org.mesonet.app.mesonetdata.dependencyinjection.MesonetUIControllerSubcomponent;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.site.SiteSelectionInterfaces;

import java.util.Map;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.FragmentKey;
import dagger.multibindings.IntoMap;



@Module(subcomponents = {MesonetFragmentSubcomponent.class,
                         MesonetUIControllerSubcomponent.class,
                         FilterListFragmentSubcomponent.class})
abstract class SiteOverviewFragmentModule {

    // TODO (ContributesAndroidInjector) remove this in favor of @ContributesAndroidInjector
    @Binds
    @IntoMap
    @FragmentKey(FilterListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    filterListFragmentInjectorFactory(FilterListFragmentSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(MesonetFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    mesonetFragmentInjectorFactory(MesonetFragmentSubcomponent.Builder builder);

    @Binds
    @PerFragment
    abstract Fragment fragment(SiteOverviewFragment inSiteOverviewFragment);

    @Binds
    @PerFragment
    abstract FilterListFragment.FilterListCloser FilterListCloser(SiteOverviewFragment inSiteOverviewFragment);

    @Binds
    @PerFragment
    abstract BaseMesonetSiteDataController MesonetSiteDataController(MesonetSiteDataController inMesonetSiteDataController);

    @Provides
    @PerFragment
    static Map<String, Pair<String, Location>> KeyToInfoMap(MesonetSiteDataController inSiteDataController)
    {
        return inSiteDataController.GetSiteList();
    }

    @Binds
    @PerFragment
    abstract SiteSelectionInterfaces.SelectSiteListener OnSelectedListener(MesonetDataController inSiteDataController);
}
