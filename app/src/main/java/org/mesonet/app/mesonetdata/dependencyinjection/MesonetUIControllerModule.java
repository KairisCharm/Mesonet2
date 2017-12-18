package org.mesonet.app.mesonetdata.dependencyinjection;

import android.app.Fragment;

import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.mesonetdata.BaseMesonetDataController;
import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.mesonetdata.MesonetUIController;

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
