package org.mesonet.app.site.caching;


import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.site.mesonetdata.MesonetSiteListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SiteListConverter
{
    @Inject
    public SiteListConverter(){}



    public List<SiteRealmModel> FavoriteToRealmModels(MesonetSiteListModel inSiteModels)
    {
        List<SiteRealmModel> result = new ArrayList<>();

        List<String> keySet = new ArrayList<>(inSiteModels.keySet());

        for(int i = 0; i < inSiteModels.size(); i++)
        {
            result.add(new SiteRealmModel(keySet.get(i), inSiteModels.get(keySet.get(i))));
        }

        return result;
    }



    public MesonetSiteListModel SiteFromRealmModels(List<SiteRealmModel> inRealmModels)
    {
        MesonetSiteListModel result = new MesonetSiteListModel();

        for(int i = 0; i < inRealmModels.size(); i++)
        {
            result.put(inRealmModels.get(i).GetStid(), inRealmModels.get(i).ToSiteModel());
        }

        return result;
    }



    public List<FavoriteSiteRealmModel> FavoriteToRealmModels(List<String> inFavorites)
    {
        List<FavoriteSiteRealmModel> result = new ArrayList<>();

        for(int i = 0; i < inFavorites.size(); i++)
        {
            result.add(new FavoriteSiteRealmModel(inFavorites.get(i)));
        }

        return result;
    }



    public List<String> FavoriteFromRealmModels(List<FavoriteSiteRealmModel> inRealmModels)
    {
        List<String> result = new ArrayList<>();

        for(int i = 0; i < inRealmModels.size(); i++)
        {
            result.add(inRealmModels.get(i).GetName());
        }

        return result;
    }
}
