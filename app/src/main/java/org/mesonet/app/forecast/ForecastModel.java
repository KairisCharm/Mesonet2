package org.mesonet.app.forecast;


import org.mesonet.app.formulas.DefaultUnits;
import org.mesonet.app.formulas.UnitConverter;

public class ForecastModel implements DefaultUnits
{
    @Override
    public UnitConverter.TempUnits GetDefaultTempUnit() {
        return UnitConverter.TempUnits.kFahrenheit;
    }

    @Override
    public UnitConverter.LengthUnits GetDefaultLengthUnit() {
        return null;
    }

    @Override
    public UnitConverter.SpeedUnits GetDefaultSpeedUnit() {
        return UnitConverter.SpeedUnits.kMps;
    }

    @Override
    public UnitConverter.PressureUnits GetDefaultPressureUnit() {
        return null;
    }

    public enum HighOrLow { High, Low }
    public String mTime;
    public String mIconUrl;
    public String mStatus;
    public HighOrLow mHighOrLow;
    public Number mTemp;
    public UnitConverter.CompassDirections mWindDirection;
    public UnitConverter.CompassDirections mWindGustsDirection;
    public Number mWindSpd;
    public Number mWindGusts;
}
