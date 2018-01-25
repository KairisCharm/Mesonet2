package org.mesonet.app.site.caching;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import org.mesonet.app.site.mesonetdata.MesonetSiteListModel;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class SiteRealmModel extends RealmObject
{
    @PrimaryKey
    private String mStid;
    private String mLat;
    private String mElev;
    private String mStnm;
    private String mName;
    private String mLon;



    public SiteRealmModel(){}



    public SiteRealmModel(String inStid, MesonetSiteListModel.MesonetSiteModel inSiteModel)
    {
        mStid = inStid;
        mLat = inSiteModel.mLat;
        mElev = inSiteModel.mElev;
        mStnm = inSiteModel.mStnm;
        mName = inSiteModel.mName;
        mLon = inSiteModel.mLon;
    }



    public MesonetSiteListModel.MesonetSiteModel ToSiteModel()
    {
        MesonetSiteListModel.MesonetSiteModel site = new MesonetSiteListModel.MesonetSiteModel();

        site.mLat = mLat;
        site.mElev = mElev;
        site.mStnm = mStnm;
        site.mName = mName;
        site.mLon = mLon;

        return site;
    }



    public String GetStid()
    {
        return mStid;
    }
}
