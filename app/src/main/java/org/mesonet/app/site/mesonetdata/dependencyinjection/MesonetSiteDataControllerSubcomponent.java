package org.mesonet.app.site.mesonetdata.dependencyinjection;

import org.mesonet.dataprocessing.site.MesonetSiteDataController;
import org.mesonet.core.PerFragment;

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
