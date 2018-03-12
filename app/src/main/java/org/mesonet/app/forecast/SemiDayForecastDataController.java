package org.mesonet.app.forecast;

import android.content.Context;
import android.content.res.Configuration;

import org.mesonet.app.R;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.formulas.DefaultUnits;
import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.userdata.Preferences;

import java.util.Locale;
import java.util.Observable;

import javax.inject.Inject;

import static org.mesonet.app.userdata.Preferences.UnitPreference.kImperial;
import static org.mesonet.app.userdata.Preferences.UnitPreference.kMetric;


public class SemiDayForecastDataController extends Observable implements ForecastData
{
    Preferences mPreferences;

    UnitConverter mUnitConverter;

    ForecastModel mForecastModel;

    Context mContext;



    public SemiDayForecastDataController(Context inContext, Preferences inPreferences, UnitConverter inUnitConverter, ForecastModel inForecastModel)
    {
        mPreferences = inPreferences;
        mUnitConverter = inUnitConverter;
        mForecastModel = inForecastModel;
        mContext = inContext;
    }

    public void SetData(ForecastModel inForecast)
    {
        mForecastModel = inForecast;

        setChanged();
        notifyObservers();
    }

    @Override
    public String GetTime() {
        String result = mForecastModel.mTime;

        if((mContext != null && mContext.getResources().getBoolean(R.bool.forceWrapForecasts)) && !result.contains(" "))
            result += "\n";

        return result;
    }

    @Override
    public String GetIconUrl() {
        return mForecastModel.mIconUrl.replace("http://www.nws.noaa.gov/weather/images/fcicons", "http://www.mesonet.org/images/fcicons-android").replace(".jpg", "@4x.png");
    }

    @Override
    public String GetStatus() {
        return mForecastModel.mStatus + "\n";
    }

    @Override
    public String GetHighOrLow() {
        return mForecastModel.mHighOrLow.name();
    }

    @Override
    public String GetTemp() {
        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences == null || mPreferences.GetUnitPreference() == kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        String unit = "";

            switch (tempUnits)
            {
                case kCelsius:
                    unit = "C";
                    break;
                case kFahrenheit:
                    unit = "F";
                    break;
            }

        if(mUnitConverter == null)
            return null;

        Integer value = mUnitConverter.GetTempInPreferredUnits(mForecastModel.mTemp, mForecastModel, tempUnits).intValue();

        return String.format(Locale.getDefault(),"%d", value) + "°" + unit;
    }



    @Override
    public String GetWindDescription()
    {
        if(mForecastModel.mWindSpd == null)
            return "Calm\n";

        UnitConverter.SpeedUnits speedUnits = UnitConverter.SpeedUnits.kMps;

        if(mPreferences == null || mPreferences.GetUnitPreference() == kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph;

        String unit = "";

        switch (speedUnits)
        {
            case kMps:
                unit = "mps";
                break;
            case kMph:
                unit = "mph";
                break;
        }

        if(mUnitConverter == null)
            return null;

        Integer value = mUnitConverter.GetSpeedInPreferredUnits(mForecastModel.mWindSpd, mForecastModel, speedUnits).intValue();

        return "Wind " + value.toString() + " at " + unit;
    }

    @Override
    public Observable GetObservable() {
        return this;
    }
}
