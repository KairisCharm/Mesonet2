package org.mesonet.app.forecast;


import org.mesonet.app.reflection.ForecastModelParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BaseFiveDayForecastDataController extends Observable
{
    List<SemiDayForecastDataController> mSemiDayForecasts = new ArrayList<>();


    public void SetData(String inForecast)
    {
        SetData(ForecastModelParser.GetInstance().Parse(inForecast));
    }


    public void SetData(List<ForecastModel> inForecast)
    {
        mSemiDayForecasts = new ArrayList<>();

        if(inForecast != null) {

            for (int i = 0; i < inForecast.size(); i++) {
                mSemiDayForecasts.add(new SemiDayForecastDataController(inForecast.get(i)));
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
