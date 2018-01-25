package org.mesonet.app.site.mesonetdata.dependencyinjection;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.site.mesonetdata.MesonetUIController;

import dagger.Subcomponent;


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
