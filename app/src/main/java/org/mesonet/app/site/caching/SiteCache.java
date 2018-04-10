package org.mesonet.app.site.caching;


import org.mesonet.app.caching.Cacher;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.site.mesonetdata.MesonetSiteListModel;

import java.util.List;

import javax.inject.Inject;


@PerActivity
public class SiteCache
{
    @Inject
    Cacher mCacher;

    @Inject
    SiteListConverter mConverter;



    @Inject
    public SiteCache(){}



    public void SaveSites(final MesonetSiteListModel inMesonetSites)
    {
        mCacher.SaveToCache(new Cacher.CacheDataProvider() {
            @Override
            public List ListToCache() {
                return mConverter.FavoriteToRealmModels(inMesonetSites);
            }

            @Override
            public Class GetClass() {
                return SiteRealmModel.class;
            }
        });
    }



    public void GetSites(final SitesCacheListener inSitesCacheListener)
    {
        mCacher.FindAll(new Cacher.FindAllListener<SiteRealmModel, MesonetSiteListModel>() {
            @Override
            public Class<SiteRealmModel> GetClass() {
                return SiteRealmModel.class;
            }

            @Override
            public void Found(MesonetSiteListModel inResults) {
                inSitesCacheListener.SitesLoaded(inResults);
            }

            @Override
            public MesonetSiteListModel Process(List<SiteRealmModel> inResults) {
                return mConverter.SiteFromRealmModels(inResults);
            }
        });
    }



    public void SaveFavorites(final List<String> inFavorites)
    {
        mCacher.SaveToCache(new Cacher.CacheDataProvider() {
            @Override
            public List ListToCache() {
                return mConverter.FavoriteToRealmModels(inFavorites);
            }

            @Override
            public Class GetClass() {
                return FavoriteSiteRealmModel.class;
            }
        });
    }



    public void GetFavorites(final FavoritesCacheListener inFavoritesCacheListener)
    {
        mCacher.FindAll(new Cacher.FindAllListener<FavoriteSiteRealmModel, List<String>>() {
            @Override
            public Class<FavoriteSiteRealmModel> GetClass() {
                return FavoriteSiteRealmModel.class;
            }

            @Override
            public void Found(List<String> inResults) {
                inFavoritesCacheListener.FavoritesLoaded(inResults);
            }

            @Override
            public List<String> Process(List<FavoriteSiteRealmModel> inResults) {
                return mConverter.FavoriteFromRealmModels(inResults);
            }
        });
    }



    public interface SitesCacheListener
    {
        void SitesLoaded(MesonetSiteListModel inResults);
    }



    public interface FavoritesCacheListener
    {
        void FavoritesLoaded(List<String> inResults);
    }
}
