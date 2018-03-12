package org.mesonet.app.site.mesonetdata;

import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.formulas.UnitConverter;
//import org.mesonet.app.site.mesonetdata.dependencyinjection.DaggerMesonetDataComponent;
import org.mesonet.app.reflection.MesonetModelParser;
import org.mesonet.app.site.caching.SiteCache;
import org.mesonet.app.userdata.Preferences;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;


@PerFragment
public abstract class BaseMesonetDataController extends Observable implements MesonetData, Observer
{
    MesonetModel mMesonetModel;

    Preferences mPreferences;

    MesonetSiteDataController mSiteDataController;

    @Inject
    SiteCache mCache;

    @Inject
    DerivedValues mDerivedValues;
    
    @Inject
    UnitConverter mUnitConverter;

    @Inject
    MesonetModelParser mModelParser;


    private boolean mUpdating = false;



    public BaseMesonetDataController(MesonetSiteDataController inSiteDataController, Preferences inPreferences)
    {
        mSiteDataController = inSiteDataController;
        mPreferences = inPreferences;
        mSiteDataController.GetDataObservable().addObserver(this);
        mPreferences.GetPreferencesObservable().addObserver(this);
        mSiteDataController.addObserver(this);
    }



    void SetData(String inMesonetDataString)
    {
        SetData(mModelParser.Parse(MesonetModel.class, inMesonetDataString));
    }



    void SetData(MesonetModel inMesonetModel)
    {
        if(!inMesonetModel.STID.toLowerCase().equals(mSiteDataController.CurrentSelection().toLowerCase()))
            return;

        mPreferences.GetPreferencesObservable().addObserver(this);
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

        if(mMesonetModel.TAIR.doubleValue() < -900.0)
            return null;

        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences == null || mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        Number result = mUnitConverter.GetTempInPreferredUnits(mMesonetModel.TAIR, mMesonetModel, tempUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double GetApparentTemp()
    {
        if(mMesonetModel == null)
            return null;

        if(mMesonetModel.TAIR.doubleValue() < -900.0)
            return null;

        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences == null || mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        Number apparentTemp = mDerivedValues.GetApparentTemperature(mMesonetModel.TAIR, mMesonetModel.WSPD, mMesonetModel.RELH);

        Number result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double GetDewpoint()
    {
        if(mMesonetModel == null)
            return null;

        if(mMesonetModel.TAIR.doubleValue() < -900.0 || mMesonetModel.RELH.doubleValue() < -900.0)
            return null;

        UnitConverter.TempUnits tempUnits = UnitConverter.TempUnits.kCelsius;

        if(mPreferences == null || mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit;

        Number apparentTemp = mDerivedValues.GetDewpoint(mMesonetModel.TAIR, mMesonetModel.RELH);

        Number result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double GetWindSpd()
    {
        if(mMesonetModel == null)
            return null;

        if(mMesonetModel.WSPD == null)
            return null;

        if(mMesonetModel.WSPD.doubleValue() < -900.0)
            return null;

        UnitConverter.SpeedUnits speedUnits = UnitConverter.SpeedUnits.kMps;

        if(mPreferences == null || mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph;

        Number result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetModel.WSPD, mMesonetModel, speedUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public UnitConverter.CompassDirections GetWindDirection()
    {
        if(mMesonetModel == null)
            return null;

        if(mMesonetModel.WDIR.doubleValue() < -900.0)
            return null;

        return mUnitConverter.GetCompassDirection(mMesonetModel.WDIR);
    }



    @Override
    public Double GetMaxWind()
    {
        if(mMesonetModel == null)
            return null;

        if(mMesonetModel.WMAX.doubleValue() < -900.0)
            return null;

        UnitConverter.SpeedUnits speedUnits = UnitConverter.SpeedUnits.kMps;

        if(mPreferences == null || mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph;

        Number result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetModel.WMAX, mMesonetModel, speedUnits);

        if(result == null)
            return null;

        return result.doubleValue();
    }



    @Override
    public Double Get24HrRain()
    {
        if(mMesonetModel == null || mMesonetModel.RAIN_24 == null)
            return null;

        if(mMesonetModel.RAIN_24H.doubleValue() < -900.0)
            return null;

        UnitConverter.LengthUnits lengthUnits = UnitConverter.LengthUnits.kMm;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            lengthUnits = UnitConverter.LengthUnits.kIn;

        Number result = mUnitConverter.GetLengthInPreferredUnits(mMesonetModel.RAIN_24H, mMesonetModel, lengthUnits);

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

        if(mMesonetModel.PRES.doubleValue() < -900.0)
            return null;

        UnitConverter.PressureUnits pressureUnits = UnitConverter.PressureUnits.kMb;

        if(mPreferences != null && mPreferences.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            pressureUnits = UnitConverter.PressureUnits.kInHg;

        Number result = mUnitConverter.GetPressureInPreferredUnits(mDerivedValues.GetMSLPressure(mMesonetModel.TAIR, mMesonetModel.PRES, mSiteDataController.CurrentStationElevation()), mMesonetModel, pressureUnits);

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
        if(mMesonetModel != null && !GetStid().toLowerCase().equals(mPreferences.GetSelectedStid()))
            StopUpdating();

        if(!IsUpdating())
            StartUpdating();

        setChanged();
        notifyObservers();
    }
}
