package org.mesonet.cache

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import org.mesonet.cache.site.mesonetdata.MesonetRealmModule
import javax.inject.Singleton
import io.realm.RealmResults
import org.mesonet.core.PerActivity
import java.util.concurrent.Executors


@PerActivity
class CacherImpl @Inject
constructor(): Cacher {
private var mRealm: Realm? = null

    val mRealmScheduler = Schedulers.from(Executors.newSingleThreadExecutor())

    internal fun Init(inContext: Context) {
        Realm.init(inContext)

        val configuration = RealmConfiguration.Builder().schemaVersion(26)
                                                                        .modules(MesonetRealmModule())
                                                                        .name(inContext.packageName)
                                                                        .build()
        mRealm = Realm.getInstance(configuration)
    }


    override fun <T: RealmModel> SaveToCache(inContext: Context, inClass: Class<T>, inList: List<T>): Observable<Void> {
        return Observable.create(ObservableOnSubscribe<Void>{
            synchronized(this@CacherImpl) {
                if(mRealm == null)
                    Init(inContext)
                mRealm?.executeTransaction { inRealm ->
                    inRealm.where(inClass).findAll().deleteAllFromRealm()
                    inRealm.copyToRealm(inList)
                }

                it.onComplete()
            }
        }).subscribeOn(mRealmScheduler)
    }


    override fun <T : RealmModel> FindAll(inContext: Context, inClass: Class<T>): Observable<RealmResults<T>> {
        return Observable.create(ObservableOnSubscribe<RealmResults<T>> {
            synchronized(this@CacherImpl) {
                if(mRealm == null)
                    Init(inContext)
                try {
                    it.onNext(mRealm?.where(inClass)?.findAll()!!)
                }
                catch (e: Exception)
                {
                    it.onError(e)
                }
            }

            it.onComplete()
        }).subscribeOn(mRealmScheduler)
    }
}