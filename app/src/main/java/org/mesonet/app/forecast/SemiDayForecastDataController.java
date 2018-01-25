package org.mesonet.app.forecast;

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
    @Inject
    Preferences mPreferences;


    ForecastModel mForecastModel;


    @Inject
    public SemiDayForecastDataController(ForecastModel inForecastModel)
    {
        mForecastModel = inForecastModel;
    }

    public void SetData(ForecastModel inForecast)
    {
        mForecastModel = inForecast;

        setChanged();
        notifyObservers();
    }

    @Override
    public String GetTime() {
        return mForecastModel.mTime;
    }

    @Override
    public String GetIconUrl() {
        return mForecastModel.mIconUrl.replace("http://www.nws.noaa.gov/weather/images/fcicons", "http://www.mesonet.org/images/fcicons-android").replace(".jpg", ".png");
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

        Integer value = UnitConverter.GetInstance().GetTempInPreferredUnits(mForecastModel.mTemp, mForecastModel, tempUnits).intValue();

        return String.format(Locale.getDefault(),"%d", value) + "°" + unit;
    }

    @Override
    public String GetWindDirection() {
        return mForecastModel.mWindDirection.name();
    }

    @Override
    public String GetWindGustsDirection() {
        return mForecastModel.mWindDirection.name();
    }

    @Override
    public String GetWindSpeed() {
        return Integer.toString(mForecastModel.mWindSpd.intValue());
    }

    @Override
    public String GetWindGusts() {
        return Integer.toString(mForecastModel.mWindGusts.intValue());
    }

    @Override
    public Observable GetObservable() {
        return this;
    }
}
