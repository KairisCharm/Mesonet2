package org.mesonet.cache.site


import org.mesonet.cache.Cacher
import org.mesonet.core.PerActivity
import org.mesonet.models.MesonetSiteListModel

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SiteCache @Inject
constructor() {
    @Inject
    internal lateinit var mCacher: Cacher

    @Inject
    internal lateinit var mConverter: SiteListConverter


    fun SaveSites(inMesonetSites: MesonetSiteListModel) {
        mCacher.SaveToCache(object : Cacher.CacheDataProvider<SiteRealmModel> {
            override fun ListToCache(): List<SiteRealmModel> {
                return mConverter.FavoriteToRealmModels(inMesonetSites)
            }

            override fun GetClass(): Class<SiteRealmModel> {
                return SiteRealmModel::class.java
            }
        })
    }


    fun GetSites(inSitesCacheListener: SitesCacheListener) {
        mCacher.FindAll(object : Cacher.FindAllListener<SiteRealmModel, MesonetSiteListModel> {
            override fun GetClass(): Class<SiteRealmModel> {
                return SiteRealmModel::class.java
            }

            override fun Found(inResults: MesonetSiteListModel) {
                inSitesCacheListener.SitesLoaded(inResults)
            }

            override fun Process(inResults: MutableList<SiteRealmModel>): MesonetSiteListModel {
                return mConverter.SiteFromRealmModels(inResults)
            }
        })
    }


    fun SaveFavorites(inFavorites: List<String>) {
        mCacher.SaveToCache(object : Cacher.CacheDataProvider<FavoriteSiteRealmModel> {
            override fun ListToCache(): List<FavoriteSiteRealmModel> {
                return mConverter.FavoriteToRealmModels(inFavorites)
            }

            override fun GetClass(): Class<FavoriteSiteRealmModel> {
                return FavoriteSiteRealmModel::class.java
            }
        })
    }


    fun GetFavorites(inFavoritesCacheListener: FavoritesCacheListener) {
        mCacher.FindAll(object : Cacher.FindAllListener<FavoriteSiteRealmModel, MutableList<String>> {
            override fun GetClass(): Class<FavoriteSiteRealmModel> {
                return FavoriteSiteRealmModel::class.java
            }

            override fun Found(inResults: MutableList<String>) {
                inFavoritesCacheListener.FavoritesLoaded(inResults)
            }

            override fun Process(inResults: MutableList<FavoriteSiteRealmModel>): MutableList<String> {
                return mConverter.FavoriteFromRealmModels(inResults)
            }
        })
    }


    interface SitesCacheListener {
        fun SitesLoaded(inSiteResults: MesonetSiteListModel)
    }


    interface FavoritesCacheListener {
        fun FavoritesLoaded(inResults: MutableList<String>)
    }
}
