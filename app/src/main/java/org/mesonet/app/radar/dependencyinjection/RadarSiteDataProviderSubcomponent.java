package org.mesonet.app.radar.dependencyinjection;


import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.radar.RadarSiteDataProvider;

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
