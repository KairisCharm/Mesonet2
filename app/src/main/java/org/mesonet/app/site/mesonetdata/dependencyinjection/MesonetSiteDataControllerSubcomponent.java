package org.mesonet.app.site.mesonetdata.dependencyinjection;


import org.mesonet.app.MainActivity;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.site.mesonetdata.MesonetDataController;
import org.mesonet.app.site.mesonetdata.MesonetSiteDataController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@PerFragment
@Subcomponent(modules = MesonetSiteDataControllerModule.class)
public interface MesonetSiteDataControllerSubcomponent extends AndroidInjector<MesonetSiteDataController>
{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MesonetSiteDataController> {
    }
}
