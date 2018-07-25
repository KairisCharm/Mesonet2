package org.mesonet.cache.site

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.cache.Cacher
import org.mesonet.core.PerActivity
import org.mesonet.models.site.MesonetSiteList

import javax.inject.Inject
import javax.inject.Singleton



@PerActivity
class SiteCacheImpl @Inject
constructor(): SiteCache {
    @Inject
    internal lateinit var mCacher: Cacher

    @Inject
    internal lateinit var mConverter: SiteListConverter


    override fun SaveSites(inContext: Context, inMesonetSites: MesonetSiteList): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void> {
            mCacher.SaveToCache(inContext, SiteRealmModel::class.java, mConverter.SitesToRealmModels(inMesonetSites)).subscribe(object: Observer<Void> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Void) {}
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })

            it.onComplete()
        }).subscribeOn(Schedulers.computation())
    }


    override fun GetSites(inContext: Context): Observable<MesonetSiteList> {
        return mCacher.FindAll(inContext, SiteRealmModel::class.java).map{
            mConverter.SitesFromRealmModels(it)
        }
    }


    override fun SaveFavorites(inContext: Context, inFavorites: List<String>): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void> {
            mCacher.SaveToCache(inContext, FavoriteSiteRealmModel::class.java, mConverter.FavoritesToRealmModels(inFavorites)).subscribe(object: Observer<Void> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Void) {}
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })

            it.onComplete()
        }).subscribeOn(Schedulers.computation())
    }


    override fun GetFavorites(inContext: Context): Observable<MutableList<String>> {
        return mCacher.FindAll(inContext, FavoriteSiteRealmModel::class.java).map {
            mConverter.FavoriteFromRealmModels(it)
        }
    }
}