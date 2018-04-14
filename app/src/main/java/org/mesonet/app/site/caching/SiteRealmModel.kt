package org.mesonet.app.site.caching

import android.util.Pair

import com.google.gson.annotations.SerializedName

import org.mesonet.app.site.mesonetdata.MesonetSiteListModel

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class SiteRealmModel : RealmObject {
    @PrimaryKey
    var mStid: String? = null
    var mLat: String? = null
    var mElev: String? = null
    var mStnm: String? = null
    var mName: String? = null
    var mLon: String? = null


    constructor() {}


    constructor(inStid: String, inSiteModel: MesonetSiteListModel.MesonetSiteModel) {
        mStid = inStid
        mLat = inSiteModel.mLat
        mElev = inSiteModel.mElev
        mStnm = inSiteModel.mStnm
        mName = inSiteModel.mName
        mLon = inSiteModel.mLon
    }


    fun ToSiteModel(): MesonetSiteListModel.MesonetSiteModel {
        val site = MesonetSiteListModel.MesonetSiteModel()

        site.mLat = mLat.toString()
        site.mElev = mElev.toString()
        site.mStnm = mStnm.toString()
        site.mName = mName.toString()
        site.mLon = mLon.toString()

        return site
    }


    fun GetStid(): String? {
        return mStid
    }
}
