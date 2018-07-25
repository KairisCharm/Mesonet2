package org.mesonet.cache

import android.content.Context
import io.reactivex.Observable
import io.realm.RealmModel
import io.realm.RealmResults

interface Cacher {
    fun <T: RealmModel> SaveToCache(inContext: Context, inClass: Class<T>, inList: List<T>): Observable<Void>
    fun <T : RealmModel> FindAll(inContext: Context, inClass: Class<T>): Observable<RealmResults<T>>
}