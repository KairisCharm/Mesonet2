package org.mesonet.androidsystem

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class PermissionsImpl @Inject
constructor(): Permissions {
    override fun LocationRequestCode(): Int {
        return 0
    }

    private val mPermissionsObservers = HashMap<String, MutableList<ObservableEmitter<Boolean>>>()


    override fun RequestPermission(inContext: Context?, inPermission: String): Observable<Boolean> {
        return Observable.create(ObservableOnSubscribe<Boolean> {
            synchronized(Permissions@this)
            {
                if (!mPermissionsObservers.containsKey(inPermission))
                    mPermissionsObservers[inPermission] = ArrayList()

                if (mPermissionsObservers[inPermission]?.contains(it) == false)
                    mPermissionsObservers[inPermission]?.add(it)

                if(inContext == null)
                {
                    it.onNext(false)
                    it.onComplete()
                    return@ObservableOnSubscribe
                }
                if (ContextCompat.checkSelfPermission(inContext, inPermission) != PackageManager.PERMISSION_GRANTED) {
                    if (inContext is Activity) {
                        ActivityCompat.requestPermissions(inContext,
                                arrayOf(inPermission),
                                0)
                    } else {
                        it.onNext(false)
                        it.onComplete()
                    }
                } else {
                    it.onNext(true)
                    it.onComplete()
                }
            }
        }).subscribeOn(Schedulers.computation())
    }


    override fun ProcessPermissionResponse(inPermissions: Array<String>, inGrantResults: IntArray) {
        Observable.create(ObservableOnSubscribe<Void> {
            for (i in inPermissions.indices) {
                if (mPermissionsObservers.containsKey(inPermissions[i])) {
                    val permissionListeners = mPermissionsObservers[inPermissions[i]]

                    val j = 0
                    if (permissionListeners != null) {
                        while (j < permissionListeners.size) {
                            permissionListeners[j].onNext(inGrantResults[i] == PackageManager.PERMISSION_GRANTED)
                            permissionListeners[j].onComplete()
                            permissionListeners.removeAt(j)
                        }
                    }
                }
            }

            it.onComplete()
        }).subscribeOn(Schedulers.computation()).subscribe(object: Observer<Void>{
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }
}