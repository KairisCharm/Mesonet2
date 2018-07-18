package org.mesonet.cache.site

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.mesonet.cache.Cacher
import org.mesonet.models.site.MesonetSiteList

import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class SiteCacheImpl @Inject
constructor(): SiteCache {
    @Inject
    internal lateinit var mCacher: Cacher

    @Inject
    internal lateinit var mConverter: SiteListConverter


    override fun SaveSites(inMesonetSites: MesonetSiteList): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void> {
            mCacher.SaveToCache(SiteRealmModel::class.java, mConverter.SitesToRealmModels(inMesonetSites)).subscribe()
        }).subscribeOn(Schedulers.computation())
    }


    override fun GetSites(): Observable<MesonetSiteList> {
        return mCacher.FindAll(SiteRealmModel::class.java).map{
            mConverter.SitesFromRealmModels(it)
        }
    }


    override fun SaveFavorites(inFavorites: List<String>): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void> {
            mCacher.SaveToCache(FavoriteSiteRealmModel::class.java, mConverter.FavoritesToRealmModels(inFavorites)).subscribe()
        }).subscribeOn(Schedulers.computation())
    }


    override fun GetFavorites(): Observable<MutableList<String>> {
        return mCacher.FindAll(FavoriteSiteRealmModel::class.java).map {
            mConverter.FavoriteFromRealmModels(it)
        }
    }
}