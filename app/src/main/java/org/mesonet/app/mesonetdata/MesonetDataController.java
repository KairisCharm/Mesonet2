package org.mesonet.app.mesonetdata;

import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.userdata.Preferences;



public class MesonetDataController implements MesonetData
{
    private MesonetModel mMesonetModel;
    private Preferences mPreferences;



    public MesonetDataController(MesonetModel inMesonetModel, Preferences inPreferences)
    {
        mMesonetModel = inMesonetModel;
        mPreferences = inPreferences;
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

        return 12345.0;
    }



    @Override
    public Double GetDewPoint()
    {
        if(mMesonetModel == null)
            return null;

        return 12345.0;
    }



    @Override
    public Double GetWindSpd()
    {
        if(mMesonetModel == null)
            return null;

        return 12345.0;
    }



    @Override
    public UnitConverter.CompassDirections GetWindDirection()
    {
        if(mMesonetModel == null)
            return null;

        return null;
    }



    @Override
    public Double GetMaxWind()
    {
        if(mMesonetModel == null)
            return null;

        return 12345.0;
    }



    @Override
    public Double Get24HrRain()
    {
        if(mMesonetModel == null)
            return null;

        return 12345.0;
    }



    @Override
    public Integer GetHumidity()
    {
        if(mMesonetModel == null)
            return null;

        return 12345;
    }



    @Override
    public Double GetPressure()
    {
        if(mMesonetModel == null)
            return null;

        return 12345.0;
    }
}
