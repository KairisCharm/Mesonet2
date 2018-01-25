package org.mesonet.app.site.mesonetdata;

import android.app.Activity;
import android.location.Location;

import kotlin.Pair;

import com.google.gson.Gson;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.MainActivity;
import org.mesonet.app.caching.Cacher;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.site.SiteSelectionInterfaces;
import org.mesonet.app.site.caching.SiteCache;
import org.mesonet.app.userdata.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;


@PerActivity
public abstract class BaseMesonetSiteDataController extends Observable implements FilterListFragment.FilterListDataProvider, SiteSelectionInterfaces.SelectSiteListener, Observer {
    @Inject
    Preferences mPreferences;

    SiteCache mCache;

    MesonetSiteListModel mMesonetSiteModel;

    List<String> mFavorites = new ArrayList<>();



    public BaseMesonetSiteDataController(SiteCache inCache)
    {
        mCache = inCache;
        mCache.GetSites(new SiteCache.SitesCacheListener() {
            @Override
            public void SitesLodaded(final MesonetSiteListModel inSiteResults) {

                mCache.GetFavorites(new SiteCache.FavoritesCacheListener() {
                    @Override
                    public void FavoritesLodaded(List<String> inResults) {
                        if(mMesonetSiteModel == null) {
                            mMesonetSiteModel = inSiteResults;
                        }

                        mFavorites = inResults;

                        if(mFavorites == null)
                            mFavorites = new ArrayList<>();

                        setChanged();
                        notifyObservers();
                    }
                });

            }
        });

        LoadData();
    }




    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers();
    }



    public void SetData(String inMesonetDataString) {
        SetData(new Gson().fromJson(inMesonetDataString, MesonetSiteListModel.class));
    }



    public void SetData(MesonetSiteListModel inMesonetSiteModel) {
        mMesonetSiteModel = inMesonetSiteModel;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");

        List<String> keys = new ArrayList<>(mMesonetSiteModel.keySet());
        for(int i = 0; i < keys.size(); i++)
        {
            Date date = null;

            try {
                date = dateFormat.parse(mMesonetSiteModel.get(keys.get(i)).mDatd);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            if(date == null || date.before(new Date()))
            {
                mMesonetSiteModel.remove(keys.get(i));
                keys.remove(i);
                i--;
            }
        }

        mCache.SaveSites(mMesonetSiteModel);

        setChanged();
        notifyObservers();
    }



    @Override
    public Map<String, BasicViewHolder.BasicViewHolderData> AllData()
    {
        if(mMesonetSiteModel == null)
            return null;
        return new MesonetStidNamePair().Create(mMesonetSiteModel, mFavorites);
    }



    @Override
    public String CurrentSelection()
    {
        return mPreferences.GetSelectedStid();
    }


    public String CurrentStationName()
    {
        String currentSelection = CurrentSelection();
        Map<String, BasicViewHolder.BasicViewHolderData> data = AllData();
        if(data == null)
            return null;
        return data.get(currentSelection).GetName();
    }



    public boolean IsFavorite(String inStid)
    {
        return mFavorites != null && mFavorites.contains(inStid);
    }



    public void AddFavorite(String inStid)
    {
        if(mFavorites != null) {
            mFavorites.add(inStid);
            mCache.SaveFavorites(mFavorites);
            setChanged();
            notifyObservers();
        }
    }



    public void RemoveFavorite(String inStid)
    {
        if(mFavorites != null && mFavorites.contains(inStid)) {
            mFavorites.remove(inStid);
            mCache.SaveFavorites(mFavorites);
            setChanged();
            notifyObservers();
        }
    }



    protected abstract void LoadData();



    public Observable GetDataObservable()
    {
        return this;
    }



    @Override
    public void SetResult(String inResult)
    {
        mPreferences.SetSelectedStid(inResult);
        setChanged();
        notifyObservers();
    }
}
