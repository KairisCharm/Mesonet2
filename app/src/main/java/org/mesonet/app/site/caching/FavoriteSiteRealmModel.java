package org.mesonet.app.site.caching;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavoriteSiteRealmModel extends RealmObject
{
    @PrimaryKey
    private String mStid;



    public String GetName()
    {
        return mStid;
    }


    public FavoriteSiteRealmModel(){}



    public FavoriteSiteRealmModel(String inStid)
    {
        mStid = inStid;
    }
}
