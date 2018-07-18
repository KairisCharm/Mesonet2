package org.mesonet.cache

import io.reactivex.Observable
import io.realm.RealmModel
import io.realm.RealmResults

interface Cacher {
    fun <T: RealmModel> SaveToCache(inClass: Class<T>, inList: List<T>): Observable<Void>
    fun <T : RealmModel> FindAll(inClass: Class<T>): Observable<RealmResults<T>>
}