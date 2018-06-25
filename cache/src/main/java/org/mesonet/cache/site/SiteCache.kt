package org.mesonet.cache.site

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.mesonet.cache.Cacher
import org.mesonet.models.site.MesonetSiteList

import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class SiteCache @Inject
constructor() {
    @Inject
    internal lateinit var mCacher: Cacher

    @Inject
    internal lateinit var mConverter: SiteListConverter


    fun SaveSites(inMesonetSites: MesonetSiteList): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void> {
            mCacher.SaveToCache(SiteRealmModel::class.java, mConverter.SitesToRealmModels(inMesonetSites))
        }).subscribeOn(Schedulers.computation())
    }


    fun GetSites(): Observable<MesonetSiteList> {
        return mCacher.FindAll(SiteRealmModel::class.java).observeOn(Schedulers.computation()).map{
            mConverter.SitesFromRealmModels(it)
        }
    }


    fun SaveFavorites(inFavorites: List<String>): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void> {
            mCacher.SaveToCache(FavoriteSiteRealmModel::class.java, mConverter.FavoritesToRealmModels(inFavorites))
        }).subscribeOn(Schedulers.computation())
    }


    fun GetFavorites(): Observable<MutableList<String>> {
        return mCacher.FindAll(FavoriteSiteRealmModel::class.java).observeOn(Schedulers.computation()).map {
            mConverter.FavoriteFromRealmModels(it)
        }
    }
}