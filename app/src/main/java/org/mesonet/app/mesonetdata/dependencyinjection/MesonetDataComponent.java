package org.mesonet.app.mesonetdata.dependencyinjection;

import org.mesonet.app.MainActivity;
import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.mesonetdata.SiteSelectionInterfaces;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.userdata.dependencyinjection.PreferencesComponent;

import dagger.Component;



@Component(modules = {MesonetDataModule.class, MainActivity.class, SiteOverviewFragment.class}, dependencies = PreferencesComponent.class)
public interface MesonetDataComponent
{
    void Inject(MesonetDataController inController);
    void Inject(MesonetFragment inMesonetFragment);
    void Inject(SiteOverviewFragment inSiteOverviewFragment);
    void Inject(MainActivity inActivity);
}
