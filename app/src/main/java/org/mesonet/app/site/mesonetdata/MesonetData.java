package org.mesonet.app.site.mesonetdata;

import org.mesonet.app.formulas.UnitConverter;

import java.util.Date;


public interface MesonetData
{
    Double GetTemp();
    Double GetApparentTemp();
    Double GetDewpoint();
    Double GetWindSpd();
    UnitConverter.CompassDirections GetWindDirection();
    Double GetMaxWind();
    Double Get24HrRain();
    Integer GetHumidity();
    Double GetPressure();
    Date GetTime ();
}
