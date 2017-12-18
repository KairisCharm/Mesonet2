package org.mesonet.app.mesonetdata;

import android.location.Location;
import kotlin.Pair;

import com.google.gson.Gson;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.dependencyinjection.PerFragment;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;



@PerFragment
public abstract class BaseMesonetSiteDataController extends Observable implements Observer {
    MesonetSiteModel mMesonetSiteModel;



    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers();
    }



    public void SetData(String inMesonetDataString) {
        SetData(new Gson().fromJson(inMesonetDataString, MesonetSiteModel.class));
    }



    public void SetData(MesonetSiteModel inMesonetSiteModel) {
        mMesonetSiteModel = inMesonetSiteModel;
        setChanged();
        notifyObservers();
    }



    public Map<String, Pair<String, Location>> GetSiteList()
    {
        return new MesonetStidNamePair().Create(mMesonetSiteModel);
    }
}
