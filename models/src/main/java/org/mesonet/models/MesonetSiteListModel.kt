package org.mesonet.models

import com.google.gson.annotations.SerializedName

import java.util.HashMap


class MesonetSiteListModel : HashMap<String, MesonetSiteListModel.MesonetSiteModel>() {
    class MesonetSiteModel {
        @SerializedName("wcs05")
        private var mWcs05: String? = null

        @SerializedName("a75")
        private var mA75: String? = null

        @SerializedName("lat")
        private var mLat: String? = null

        @SerializedName("wcs60")
        private var mWcs60: String? = null

        @SerializedName("wcs75")
        private var mWcs75: String? = null

        @SerializedName("wcr05")
        private var mWcr05: String? = null

        @SerializedName("elev")
        private var mElev: String? = null

        @SerializedName("a10")
        private var mA10: String? = null

        @SerializedName("wcs10")
        private var mWcs10: String? = null

        @SerializedName("datc")
        private var mDatc: String? = null

        @SerializedName("datd")
        private var mDatd: String? = null

        @SerializedName("n75")
        private var mN75: String? = null

        @SerializedName("n25")
        private var mN25: String? = null

        @SerializedName("wcs25")
        private var mWcs25: String? = null

        @SerializedName("n60")
        private var mN60: String? = null

        @SerializedName("wcr25")
        private var mWcr25: String? = null

        @SerializedName("a05")
        private var mA05: String? = null

        @SerializedName("stnm")
        private var mStnm: String? = null

        @SerializedName("a60")
        private var mA60: String? = null

        @SerializedName("wcr60")
        private var mWcr60: String? = null

        @SerializedName("a25")
        private var mA25: String? = null

        @SerializedName("name")
        private var mName: String? = null

        @SerializedName("n10")
        private var mN10: String? = null

        @SerializedName("n05")
        private var mN05: String? = null

        @SerializedName("wcr10")
        private var mWcr10: String? = null

        @SerializedName("cldv")
        private var mCldv: String? = null

        @SerializedName("wcr75")
        private var mWcr75: String? = null

        @SerializedName("lon")
        private var mLon: String? = null

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
