package org.mesonet.app.radar.dependencyinjection;

import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.radar.RadarDataController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;



@PerFragment
@Subcomponent(modules = RadarFragmentModule.class)
public interface RadarDataControllerSubcomponent extends AndroidInjector<RadarDataController>
{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RadarDataController> {
    }
}
