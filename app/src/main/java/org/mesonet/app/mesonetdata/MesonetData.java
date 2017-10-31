package org.mesonet.app.mesonetdata;


import org.mesonet.app.formulas.UnitConverter;

public interface MesonetData
{
    Double GetTemp();
    Double GetApparentTemp();
    Double GetDewPoint();
    Double GetWindSpd();
    UnitConverter.CompassDirections GetWindDirection();
    Double GetMaxWind();
    Double Get24HrRain();
    Integer GetHumidity();
    Double GetPressure();
}
