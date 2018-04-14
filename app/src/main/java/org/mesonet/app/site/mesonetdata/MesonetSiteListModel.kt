package org.mesonet.app.site.mesonetdata

import com.google.gson.annotations.SerializedName

import java.util.HashMap


class MesonetSiteListModel : HashMap<String, MesonetSiteListModel.MesonetSiteModel>() {
    class MesonetSiteModel {
        @SerializedName("wcs05")
        var mWcs05: String? = null

        @SerializedName("a75")
        var mA75: String? = null

        @SerializedName("lat")
        var mLat: String? = null

        @SerializedName("wcs60")
        var mWcs60: String? = null

        @SerializedName("wcs75")
        var mWcs75: String? = null

        @SerializedName("wcr05")
        var mWcr05: String? = null

        @SerializedName("elev")
        var mElev: String? = null

        @SerializedName("a10")
        var mA10: String? = null

        @SerializedName("wcs10")
        var mWcs10: String? = null

        @SerializedName("datc")
        var mDatc: String? = null

        @SerializedName("datd")
        var mDatd: String? = null

        @SerializedName("n75")
        var mN75: String? = null

        @SerializedName("n25")
        var mN25: String? = null

        @SerializedName("wcs25")
        var mWcs25: String? = null

        @SerializedName("n60")
        var mN60: String? = null

        @SerializedName("wcr25")
        var mWcr25: String? = null

        @SerializedName("a05")
        var mA05: String? = null

        @SerializedName("stnm")
        var mStnm: String? = null

        @SerializedName("a60")
        var mA60: String? = null

        @SerializedName("wcr60")
        var mWcr60: String? = null

        @SerializedName("a25")
        var mA25: String? = null

        @SerializedName("name")
        var mName: String? = null

        @SerializedName("n10")
        var mN10: String? = null

        @SerializedName("n05")
        var mN05: String? = null

        @SerializedName("wcr10")
        var mWcr10: String? = null

        @SerializedName("cldv")
        var mCldv: String? = null

        @SerializedName("wcr75")
        var mWcr75: String? = null

        @SerializedName("lon")
        var mLon: String? = null
    }
}
