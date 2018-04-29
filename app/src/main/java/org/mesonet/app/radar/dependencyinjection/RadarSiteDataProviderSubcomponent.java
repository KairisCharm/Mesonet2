package org.mesonet.app.radar.dependencyinjection;


import org.mesonet.dataprocessing.radar.RadarSiteDataProvider;
import org.mesonet.core.PerFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerFragment
@Subcomponent
public interface RadarSiteDataProviderSubcomponent extends AndroidInjector<RadarSiteDataProvider>
{
    @Subcomponent.Builder
    abstract class Builder
    {
        abstract RadarSiteDataProviderSubcomponent GetSubcomponent();
    }
}
