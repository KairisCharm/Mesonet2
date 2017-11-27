package org.mesonet.app.mesonetdata;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;



public class MesonetSiteDataController extends Observable implements Observer {
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



    public Map<String, String> GetSiteList()
    {
        return new MesonetStidNamePair().Create(mMesonetSiteModel);
    }
}
