package org.mesonet.app.site.caching


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FavoriteSiteRealmModel : RealmObject {
    @PrimaryKey
    var mStid: String? = null


    fun GetName(): String? {
        return mStid
    }


    constructor() {}


    constructor(inStid: String) {
        mStid = inStid
    }
}
