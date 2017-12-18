package org.mesonet.app.mesonetdata;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.formulas.UnitConverter;
//import org.mesonet.app.mesonetdata.dependencyinjection.DaggerMesonetDataComponent;
import org.mesonet.app.site.SiteSelectionInterfaces;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;


@PerFragment
public abstract class BaseMesonetDataController extends Observable implements MesonetData, SiteSelectionInterfaces.SelectSiteListener, Observer
{
    MesonetModel mMesonetModel;

    @Inject
    Preferences mPreferences;

    @Inject
    PreferencesObservable mObservable;

    private boolean mUpdating = false;



    public BaseMesonetDataController()
    {
//        mObservable.addObserver(this);
    }



    void SetData(String inMesonetDataString)
    {
        SetData(MesonetModel.NewInstance(inMesonetDataString));
    }



    void SetData(MesonetModel inMesonetModel)
    {
        mObservable.addObserver(this);
        mMesonetModel = inMesonetModel;
        setChanged();
        notifyObservers();
    }



    public void StartUpdating()
    {
        mUpdating = true;
    }



    public void StopUpdating()
    {
        mUpdating = false;
    }



    protected boolean IsUpdating()
    {
        return mUpdating;
    }



    @Override
    public Double GetTemp()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        Number result = UnitConverter.GetInstance().GetTempInPreferredUnits(mMesonetModel.TAIR, mMesonetModel, tempUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double GetApparentTemp()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        Number apparentTemp = DerivedValues.GetInstance().GetApparentTemperature(mMesonetModel.TAIR, mMesonetModel.WSPD, mMesonetModel.RELH);

        Number result = UnitConverter.GetInstance().GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double GetDewPoint()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        Number apparentTemp = DerivedValues.GetInstance().GetDewPoint(mMesonetModel.TAIR, mMesonetModel.RELH);

        Number result = UnitConverter.GetInstance().GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double GetWindSpd()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.SpeedUnits speedUnits = UnitConverter.SpeedUnits.kMps;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph;

        Number result = UnitConverter.GetInstance().GetSpeedInPreferredUnits(mMesonetModel.WSPD, mMesonetModel, speedUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public UnitConverter.CompassDirections GetWindDirection()
    {
        if(mMesonetModel == null)
            return null;

        return UnitConverter.GetInstance().GetCompassDirection(mMesonetModel.WDIR);
    }



    @Override
    public Double GetMaxWind()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.SpeedUnits speedUnits = UnitConverter.SpeedUnits.kMps;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph;

        Number result = UnitConverter.GetInstance().GetSpeedInPreferredUnits(mMesonetModel.WMAX, mMesonetModel, speedUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double Get24HrRain()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.LengthUnits lengthUnits = UnitConverter.LengthUnits.kMm;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            lengthUnits = UnitConverter.LengthUnits.kIn;

        Number result = UnitConverter.GetInstance().GetLengthInPreferredUnits(mMesonetModel.RAIN_24H, mMesonetModel, lengthUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Integer GetHumidity()
    {
        if(mMesonetModel == null || mMesonetModel.RELH == null)
            return null;

        return mMesonetModel.RELH.intValue();
    }



    @Override
    public Double GetPressure()
    {
        if(mMesonetModel == null)
            return null;

        UnitConverter.PressureUnits pressureUnits = UnitConverter.PressureUnits.kMmHg;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            pressureUnits = UnitConverter.PressureUnits.kInHg;

        Number result = UnitConverter.GetInstance().GetPressureInPreferredUnits(mMesonetModel.PRES, mMesonetModel, pressureUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }


    /*TODO begin add tests*/
    @Override
    public Date GetTime ()
    {
        if(mMesonetModel == null || mMesonetModel.TIME == null)
            return null;

        return new Date(mMesonetModel.TIME * 1000);
    }



    protected String GetStid ()
    {
        if(mMesonetModel == null)
            return null;

        return mMesonetModel.STID;
    }
    /*TODO end add tests*/



    public Preferences.UnitPreference GetUnitPreference()
    {
        if(mPreferences == null)
            return null;

        return mPreferences.GetUnitPreference();
    }



    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers();
    }
}
