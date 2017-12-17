package org.mesonet.app.dependencyinjection;

import org.mesonet.app.site.SiteOverviewFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerFragment
@Subcomponent(modules = SiteOverviewFragmentModule.class)
public interface SiteOverviewFragmentSubcomponent extends AndroidInjector<SiteOverviewFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SiteOverviewFragment> {
    }
}