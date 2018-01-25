package org.mesonet.app.site.mesonetdata.dependencyinjection;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.site.mesonetdata.BaseMesonetDataController;
import org.mesonet.app.site.mesonetdata.MesonetDataController;
import org.mesonet.app.site.mesonetdata.MesonetUIController;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class MesonetUIControllerModule
{
    @Binds
    @PerChildFragment
    abstract MesonetUIController MesonetUIController(MesonetUIController inMesonetUIController);

    @Binds
    @PerChildFragment
    abstract BaseMesonetDataController BaseMesonetDataController(MesonetDataController inMesonetDataController);
}
