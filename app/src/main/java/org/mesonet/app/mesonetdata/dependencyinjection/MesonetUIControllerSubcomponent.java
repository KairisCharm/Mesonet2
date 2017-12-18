package org.mesonet.app.mesonetdata.dependencyinjection;

import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.mesonetdata.MesonetUIController;

import org.mesonet.app.dependencyinjection.MainActivityModule;

import dagger.Component;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@PerChildFragment
@Subcomponent(modules = {MesonetUIControllerModule.class})
public interface MesonetUIControllerSubcomponent
{
    void Inject(MesonetUIController inMesonetUIController);

    @Subcomponent.Builder
    abstract class Builder
    {
        abstract MesonetUIControllerSubcomponent GetSubcomponent();
    }
}
