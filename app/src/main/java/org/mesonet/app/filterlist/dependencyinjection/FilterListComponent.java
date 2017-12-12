package org.mesonet.app.filterlist.dependencyinjection;

import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.mesonetdata.dependencyinjection.MesonetDataModule;
import org.mesonet.app.site.SiteOverviewFragment;

import dagger.Component;



@Component(modules = {MesonetDataModule.class, SiteOverviewFragment.class})
public interface FilterListComponent
{
    void Inject(FilterListFragment inFragment);
}
