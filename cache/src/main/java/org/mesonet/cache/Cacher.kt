package org.mesonet.cache

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import org.mesonet.cache.site.mesonetdata.MesonetRealmModule
import javax.inject.Singleton
import io.realm.RealmResults
import java.util.concurrent.Executors


@Singleton
class Cacher @Inject
constructor(inContext: Context) {
private var mRealm: Realm? = null

    val mRealmScheduler = Schedulers.from(Executors.newSingleThreadExecutor())

    init {
        Observable.create(ObservableOnSubscribe<Void>{
        synchronized(this@Cacher) {
            Realm.init(inContext)

            val configuration = RealmConfiguration.Builder().schemaVersion(26)
                                                                            .modules(MesonetRealmModule())
                                                                            .name(inContext.packageName)
                                                                            .build()
            mRealm = Realm.getInstance(configuration)

            false
            }
        }).subscribeOn(mRealmScheduler).subscribe()
    }


    fun <T: RealmModel> SaveToCache(inClass: Class<T>, inList: List<T>): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void>{
            synchronized(this@Cacher) {
                mRealm?.executeTransaction { inRealm ->
                    inRealm.where(inClass).findAll().deleteAllFromRealm()
                    inRealm.copyToRealm(inList)
                }

            false
            }
        }).subscribeOn(mRealmScheduler)
    }


    fun <T : RealmModel> FindAll(inClass: Class<T>): Observable<RealmResults<T>> {
        return Observable.create(ObservableOnSubscribe<RealmResults<T>> {
            synchronized(this@Cacher) {
                it.onNext(mRealm!!.where(inClass).findAll())
            }
        }).subscribeOn(mRealmScheduler)
    }
}