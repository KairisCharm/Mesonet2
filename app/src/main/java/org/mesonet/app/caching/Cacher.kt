package org.mesonet.app.caching

import android.app.Activity
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message

import org.mesonet.app.dependencyinjection.PerActivity

import java.util.ArrayList

import javax.inject.Inject

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.where


@PerActivity
class Cacher @Inject
constructor(inContext: Activity) {
    private var mRealm: Realm? = null
    private val mRealmThread = HandlerThread("Realm")


    init {
        mRealmThread.start()
        Handler(mRealmThread.looper, Handler.Callback {
            synchronized(this@Cacher) {
                Realm.init(inContext)
                val configuration = RealmConfiguration.Builder().schemaVersion(26)
                        .modules(MesonetRealmModule())
                        .name(inContext.packageName)
                        .build()
                mRealm = Realm.getInstance(configuration)

                false
            }
        }).sendEmptyMessage(0)
    }


    fun SaveToCache(inCacheController: CacheDataProvider<*>) {
        Handler(mRealmThread.looper, Handler.Callback {
            synchronized(this@Cacher) {

                mRealm?.executeTransaction { inRealm ->
                    inRealm.where<RealmModel>(inCacheController.GetClass() as Class<RealmModel>?).findAll().deleteAllFromRealm()
                    inRealm.copyToRealm(inCacheController.ListToCache())
                }

                false
            }
        }).sendEmptyMessage(0)
    }


    fun <T : RealmModel, U> FindAll(inListener: FindAllListener<T, U>) {
        val callingLooper = Looper.myLooper()
        Handler(mRealmThread.looper, Handler.Callback {
            synchronized(this@Cacher) {
                val result = inListener.Process(mRealm!!.where(inListener.GetClass()).findAll())

                Handler(callingLooper, Handler.Callback {
                    inListener.Found(result)
                    false
                }).sendEmptyMessage(0)

                false
            }
        }).sendEmptyMessage(0)
    }


    fun finalize() {
        if (mRealm != null)
            mRealm!!.close()

        mRealmThread?.quit()
    }


    interface FindAllListener<T : RealmModel, U> : CacheController<T> {
        fun Found(inResults: U)
        fun Process(inResults: MutableList<T>): U
    }


    interface CacheDataProvider<T : RealmModel> : CacheController<T> {
        fun ListToCache(): List<T>
    }


    interface CacheController<T : RealmModel> {
        fun GetClass(): Class<T>
    }
}
