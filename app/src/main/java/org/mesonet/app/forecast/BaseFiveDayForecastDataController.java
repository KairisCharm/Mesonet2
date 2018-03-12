package org.mesonet.app.forecast;


import android.app.Activity;
import android.content.Context;

import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.reflection.ForecastModelParser;
import org.mesonet.app.userdata.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

public class BaseFiveDayForecastDataController extends Observable
{
    @Inject
    ForecastModelParser mModelParser;

    @Inject
    Preferences mPreferences;

    @Inject
    UnitConverter mUnitConverter;

    @Inject
    Activity mActivity;

    List<SemiDayForecastDataController> mSemiDayForecasts = new ArrayList<>();


    @Inject
    BaseFiveDayForecastDataController(){}


    public void SetData(String inForecast)
    {
        SetData(mModelParser.Parse(inForecast));
    }


    public void SetData(List<ForecastModel> inForecast)
    {
        mSemiDayForecasts = new ArrayList<>();

        if(inForecast != null) {

            for (int i = 0; i < inForecast.size(); i++) {
                mSemiDayForecasts.add(new SemiDayForecastDataController(mActivity, mPreferences, mUnitConverter, inForecast.get(i)));
            }
        }

        setChanged();
        notifyObservers();
    }



    public int GetCount()
    {
        return mSemiDayForecasts.size();
    }



    public SemiDayForecastDataController GetForecast(int inIndex)
    {
        return mSemiDayForecasts.get(inIndex);
    }



    public Observable GetObservable()
    {
        return this;
    }
}
