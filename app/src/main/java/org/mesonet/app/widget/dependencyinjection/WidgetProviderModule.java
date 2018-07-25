package org.mesonet.app.widget.dependencyinjection;

import android.content.Context;

import org.mesonet.app.Application;
import org.mesonet.app.widget.EmptyLocationProvider;
import org.mesonet.app.widget.WidgetProvider;
import org.mesonet.app.widget.WidgetProviderLarge;
import org.mesonet.cache.Cacher;
import org.mesonet.cache.CacherImpl;
import org.mesonet.cache.site.SiteCache;
import org.mesonet.cache.site.SiteCacheImpl;
import org.mesonet.core.PerActivity;
import org.mesonet.dataprocessing.LocationProvider;
import org.mesonet.dataprocessing.site.MesonetSiteDataController;
import org.mesonet.dataprocessing.site.MesonetSiteDataControllerImpl;
import org.mesonet.dataprocessing.site.mesonetdata.MesonetDataController;
import org.mesonet.dataprocessing.site.mesonetdata.MesonetDataControllerImpl;
import org.mesonet.dataprocessing.site.mesonetdata.MesonetUIController;
import org.mesonet.dataprocessing.site.mesonetdata.MesonetUIControllerImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = AndroidSupportInjectionModule.class)
public abstract class WidgetProviderModule
{
    @Binds
    @PerActivity
    abstract LocationProvider LocationProvider(EmptyLocationProvider inEmptyLocationProvider);

    @Binds
    @PerActivity
    abstract Context Context(Application inContext);

    @Binds
    @PerActivity
    abstract MesonetSiteDataController MesonetSiteDataController(MesonetSiteDataControllerImpl inProvider);

    @Binds
    @PerActivity
    abstract MesonetDataController MesonetDataController(MesonetDataControllerImpl inProvider);

    @Binds
    @PerActivity
    abstract MesonetUIController MesonetUIController(MesonetUIControllerImpl inProvider);

    @Binds
    @PerActivity
    abstract SiteCache SiteCache(SiteCacheImpl inCache);

    @Binds
    @PerActivity
    abstract Cacher Cacher(CacherImpl inCache);
}
