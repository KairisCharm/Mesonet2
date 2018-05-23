package org.mesonet.models.site

import com.google.gson.annotations.SerializedName

import java.util.HashMap


class MesonetSiteListModel : HashMap<String, MesonetSiteListModel.MesonetSiteModel>(), MesonetSiteList {
    override fun GetSite(inStid: String): MesonetSiteModel? {
        return get(inStid)
    }


    class MesonetSiteModel {

        @SerializedName("lat")
        internal var mLat: String? = null

        @SerializedName("elev")
        internal var mElev: String? = null

        @SerializedName("datd")
        internal var mDatd: String? = null

        @SerializedName("stnm")
        internal var mStnm: String? = null

        @SerializedName("name")
        internal var mName: String? = null

        @SerializedName("lon")
        internal var mLon: String? = null

        fun GetLat(): String?
        {
            return mLat
        }

        fun GetElev(): String?
        {
            return mElev
        }

        fun GetStnm(): String?
        {
            return mStnm
        }

        fun GetName(): String?
        {
            return mName
        }

        fun GetLon(): String?
        {
            return mLon
        }


        fun GetDatd(): String?
        {
            return mDatd
        }


        fun SetLat(inLat: String?)
        {
            mLat = inLat
        }

        fun SetElev(inElev: String?)
        {
            mElev = inElev
        }

        fun SetStnm(inStnm: String?)
        {
            mStnm = inStnm
        }

        fun SetName(inName: String?)
        {
            mName = inName
        }

        fun SetLon(inLon: String?)
        {
            mLon = inLon
        }
    }
}
