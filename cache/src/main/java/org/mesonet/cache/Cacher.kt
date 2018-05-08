package org.mesonet.cache

import android.content.Context
import android.app.Activity
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

import org.mesonet.core.PerActivity

import javax.inject.Inject

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import org.mesonet.cache.site.mesonetdata.MesonetRealmModule
import org.mesonet.core.ThreadHandler
import javax.inject.Singleton
import android.content.ComponentName
import android.app.ActivityManager




@Singleton
class Cacher @Inject
constructor(internal var mThreadHandler: ThreadHandler, inContext: Context) {
    private var mRealm: Realm? = null


    init {
        mThreadHandler.RunOnSpecificThread("Cache", Runnable {
            synchronized(this@Cacher) {
                Realm.init(inContext)

                val configuration = RealmConfiguration.Builder().schemaVersion(26)
                        .modules(MesonetRealmModule())
                        .name(inContext.packageName)
                        .build()
                mRealm = Realm.getInstance(configuration)

                false
            }
        })
    }


    fun SaveToCache(inCacheController: CacheDataProvider<*>) {
        mThreadHandler.RunOnSpecificThread("Cache", Runnable {
            synchronized(this@Cacher) {

                mRealm?.executeTransaction { inRealm ->
                    inRealm.where(inCacheController.GetClass()).findAll().deleteAllFromRealm()
                    inRealm.copyToRealm(inCacheController.ListToCache())
                }

                false
            }
        })
    }


    fun <T : RealmModel, U> FindAll(inListener: FindAllListener<T, U>) {
        var result: U? = null
        mThreadHandler.RunOnSpecificThread("Cache", Runnable {
            synchronized(this@Cacher) {
                result = inListener.Process(mRealm!!.where(inListener.GetClass()).findAll())
            }
        }, Runnable {
            if(result != null)
                inListener.Found(result!!)
        })
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
