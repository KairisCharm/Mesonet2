package org.mesonet.app.site.mesonetdata;


import org.mesonet.app.formulas.DefaultUnits;
import org.mesonet.app.formulas.UnitConverter;

public class MesonetModel implements DefaultUnits
{
    Integer STNM;
    Long TIME;
    Number RELH;
    Number TAIR;
    Number WSPD;
    Number WVEC;
    Number WDIR;
    Number WDSD;
    Number WSSD;
    Number WMAX;
    Number RAIN;
    Number PRES;
    Number SRAD;
    Number TA9M;
    Number WS2M;
    Number TS10;
    Number TB10;
    Number TS05;
    Number TS25;
    Number TS60;
    Number TR05;
    Number TR25;
    Number TR60;
    Number RAIN_24H;
    Number RAIN_24;
    String STID;
    Integer YEAR;
    Integer MONTH;
    Integer DAY;
    Integer HOUR;
    Integer MINUTE;
    Integer SECOND;



    @Override
    public UnitConverter.TempUnits GetDefaultTempUnit()
    {
        return UnitConverter.TempUnits.kCelsius;
    }



    @Override
    public UnitConverter.LengthUnits GetDefaultLengthUnit()
    {
        return UnitConverter.LengthUnits.kMm;
    }



    @Override
    public UnitConverter.SpeedUnits GetDefaultSpeedUnit()
    {
        return UnitConverter.SpeedUnits.kMps;
    }



    @Override
    public UnitConverter.PressureUnits GetDefaultPressureUnit()
    {
        return UnitConverter.PressureUnits.kMb;
    }
}