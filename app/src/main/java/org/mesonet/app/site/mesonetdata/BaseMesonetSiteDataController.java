package org.mesonet.app.site.mesonetdata;

import android.location.Location;

import com.google.gson.Gson;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.androidsystem.DeviceLocation;
import org.mesonet.app.androidsystem.Permissions;
import org.mesonet.app.dependencyinjection.PerActivity;
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


@PerActivity
public abstract class BaseMesonetSiteDataController extends Observable implements FilterListFragment.FilterListDataProvider, SiteSelectionInterfaces.SelectSiteListener, Observer {
    @Inject
    Preferences mPreferences;

    @Inject
    Permissions mPermissions;

    @Inject
    DeviceLocation mDeviceLocation;



    SiteCache mCache;

    MesonetSiteListModel mMesonetSiteModel;

    List<String> mFavorites = new ArrayList<>();



    public BaseMesonetSiteDataController(DeviceLocation inDeviceLocation, SiteCache inCache)
    {
        mDeviceLocation = inDeviceLocation;
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
            catch (ParseException | NullPointerException e)
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

        mDeviceLocation.GetLocation(new DeviceLocation.LocationListener() {
            @Override
            public void LastLocationFound(Location inLocation) {
                mPreferences.SetSelectedStid(GetNearestSite(inLocation));
                setChanged();
                notifyObservers();
            }

            @Override
            public void LocationUnavailable() {
                setChanged();
                notifyObservers();
            }
        });
    }



    private String GetNearestSite(Location inLocation)
    {
        List<String> keys = new ArrayList<>(mMesonetSiteModel.keySet());

        int shortestDistanceIndex = -1;
        float shortestDistance = Float.MAX_VALUE;

        for(int i = 0; i < keys.size(); i++)
        {
            MesonetSiteListModel.MesonetSiteModel site = mMesonetSiteModel.get(keys.get(i));

            Location siteLocation = new Location("none");

            siteLocation.setLatitude(Double.parseDouble(site.mLat));
            siteLocation.setLongitude(Double.parseDouble(site.mLon));

            float distance = siteLocation.distanceTo(inLocation);

            if(distance < shortestDistance)
            {
                shortestDistance = distance;
                shortestDistanceIndex = i;
            }
        }

        if(shortestDistanceIndex == -1)
            return null;

        return keys.get(shortestDistanceIndex);
    }



    @Override
    public Map<String, BasicViewHolder.BasicViewHolderData> AllViewHolderData()
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


    public boolean SiteDataFound()
    {
        return mMesonetSiteModel != null && mMesonetSiteModel.size() > 0;
    }


    public String CurrentStationName()
    {
        String currentSelection = CurrentSelection();

        if(currentSelection == null || mMesonetSiteModel == null || !mMesonetSiteModel.containsKey(currentSelection))
            return null;
        return mMesonetSiteModel.get(currentSelection).mName;
    }



    public Number CurrentStationElevation()
    {
        String currentSelection = CurrentSelection();

        if(currentSelection == null ||
                mMesonetSiteModel == null ||
                !mMesonetSiteModel.containsKey(currentSelection) ||
                mMesonetSiteModel.get(currentSelection) == null ||
                mMesonetSiteModel.get(currentSelection).mElev == null)
            return null;

        Double result = null;

        try
        {
            result = Double.parseDouble(mMesonetSiteModel.get(currentSelection).mElev);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        return result;
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
