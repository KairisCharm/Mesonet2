package org.mesonet.app.mesonetdata;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;



public class MesonetSiteModel extends HashMap<String, MesonetSiteModel.MesonetSiteDetailModel>
{
    public static class MesonetSiteDetailModel
    {
        @SerializedName("wcs05")
        public String mWcs05;

        @SerializedName("a75")
        public String mA75;

        @SerializedName("lat")
        public String mLat;

        @SerializedName("wcs60")
        public String mWcs60;

        @SerializedName("wcs75")
        public String mWcs75;

        @SerializedName("wcr05")
        public String mWcr05;

        @SerializedName("elev")
        public String mElev;

        @SerializedName("a10")
        public String mA10;

        @SerializedName("wcs10")
        public String mWcs10;

        @SerializedName("datc")
        public String mDatc;

        @SerializedName("datd")
        public String mDatd;

        @SerializedName("n75")
        public String mN75;

        @SerializedName("n25")
        public String mN25;

        @SerializedName("wcs25")
        public String mWcs25;

        @SerializedName("n60")
        public String mN60;

        @SerializedName("wcr25")
        public String mWcr25;

        @SerializedName("a05")
        public String mA05;

        @SerializedName("stnm")
        public String mStnm;

        @SerializedName("a60")
        public String mA60;

        @SerializedName("wcr60")
        public String mWcr60;

        @SerializedName("a25")
        public String mA25;

        @SerializedName("name")
        public String mName;

        @SerializedName("n10")
        public String mN10;

        @SerializedName("n05")
        public String mN05;

        @SerializedName("wcr10")
        public String mWcr10;

        @SerializedName("cldv")
        public String mCldv;

        @SerializedName("wcr75")
        public String mWcr75;

        @SerializedName("lon")
        public String mLon;
    }
}
