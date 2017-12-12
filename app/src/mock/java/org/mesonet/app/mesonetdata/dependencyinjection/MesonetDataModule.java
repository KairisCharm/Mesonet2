package org.mesonet.app.mesonetdata.dependencyinjection;

import org.mesonet.app.mesonetdata.MesonetData;
import org.mesonet.app.mesonetdata.MesonetSiteDataController;
import org.mesonet.app.mesonetdata.MockMesonetDataController;
import org.mesonet.app.mesonetdata.MockMesonetSiteDataController;

import dagger.Module;
import dagger.Provides;



@Module
public class MesonetDataModule
{
    @Provides
    MesonetData GetMesonetData()
    {
        return new MockMesonetDataController();
    }

    @Provides
    MesonetSiteDataController GetMesonetSiteData() { return new MockMesonetSiteDataController(); }
}
