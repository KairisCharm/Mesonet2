package org.mesonet.androidsystem


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Permissions @Inject
constructor() {
    private val mPermissionsObservers = HashMap<String, MutableList<Observer<Boolean>>>()


    fun RequestPermission(inContext: Context, inPermission: String): Observable<Boolean> {
        return Observable.create(ObservableOnSubscribe<Boolean> {
            synchronized(Permissions@this)
            {
                if (!mPermissionsObservers.containsKey(inPermission))
                    mPermissionsObservers[inPermission] = ArrayList()

                if (!mPermissionsObservers[inPermission]!!.contains(it as Observer<*>))
                    mPermissionsObservers[inPermission]!!.add(it as Observer<Boolean>)

                if (ContextCompat.checkSelfPermission(inContext, inPermission) != PackageManager.PERMISSION_GRANTED) {
                    if (inContext is Activity) {
                        ActivityCompat.requestPermissions(inContext,
                                arrayOf(inPermission),
                                0)
                    } else {
                        it.onNext(false)
                    }
                } else {
                    it.onNext(false)
                }
            }
        }).observeOn(Schedulers.computation())
    }


    fun ProcessPermissionResponse(inPermissions: Array<String>, inGrantResults: IntArray) {
        Observable.create(ObservableOnSubscribe<Void> {
            for (i in inPermissions.indices) {
                if (mPermissionsObservers.containsKey(inPermissions[i])) {
                    val permissionListeners = mPermissionsObservers[inPermissions[i]]

                    val j = 0
                    if (permissionListeners != null) {
                        while (j < permissionListeners.size) {
                            permissionListeners.get(j).onNext(inGrantResults[i] == PackageManager.PERMISSION_GRANTED)
                            permissionListeners.removeAt(j)
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.computation()).subscribe()
    }
}