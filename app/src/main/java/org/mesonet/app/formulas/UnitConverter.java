package org.mesonet.app.formulas;



public class UnitConverter
{
    public enum TempUnits { kCelsius, kFahrenheit }
    public enum SpeedUnits { kMps, kKmph, kMph }
    public enum LengthUnits { kMm, kIn }
    public enum PressureUnits { kMmHg, kInHg }

    public enum CompassDirections { N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW }



    private static UnitConverter sUnitConverter;



    public static UnitConverter GetInstance()
    {
        if(sUnitConverter == null)
            sUnitConverter = new UnitConverter();

        return sUnitConverter;
    }



    private UnitConverter(){}



    public Number CelsiusToFahrenheit(Number inCelsius)
    {
        if(inCelsius == null)
            return null;

        return inCelsius.doubleValue() * (9.0 / 5.0) + 32.0;
    }



    public Number FahrenheitToCelsius(Number inFahrenheit)
    {
        if(inFahrenheit == null)
            return null;

        return (inFahrenheit.doubleValue() - 32.0) / (9.0 / 5.0);
    }



    public Number MpsToMph(Number inMps)
    {
        if(inMps == null)
            return null;

        return inMps.doubleValue() * 2.236936292054402;
    }



    public Number MphToMps(Number inMph)
    {
        if(inMph == null)
            return null;

        return inMph.doubleValue() * (1.0 / MpsToMph(1).doubleValue());
    }



    public Number MpsToKmph(Number inMps)
    {
        if(inMps == null)
            return null;

        return inMps.doubleValue() * 3.6;
    }



    public Number KmphToMps(Number inKmph)
    {
        if(inKmph == null)
            return null;

        return inKmph.doubleValue() * (1.0 / MpsToKmph(1).doubleValue());
    }



    public Number MphToKmph(Number inMph)
    {
        if(inMph == null)
            return null;

        return inMph.doubleValue() * 1.609344;
    }



    public Number KmphToMph(Number inMph)
    {
        if(inMph == null)
            return null;

        return inMph.doubleValue() * (1.0 / MphToKmph(1).doubleValue());
    }



    public Number MmToIn(Number inMm)
    {
        if(inMm == null)
            return null;

        return inMm.doubleValue() * 0.0393700787401575;
    }



    public Number InToMm(Number inIn)
    {
        if(inIn == null)
            return null;

        return inIn.doubleValue() * (1.0 / MmToIn(1).doubleValue());
    }



    public Number GetTempInPreferredUnits(Number inBaseValue, DefaultUnits inDefaultUnits, TempUnits inPreference)
    {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultTempUnit() == null || inPreference == null)
            return null;

        if (inPreference == inDefaultUnits.GetDefaultTempUnit())
            return inBaseValue;

        switch (inDefaultUnits.GetDefaultTempUnit())
        {
            case kFahrenheit:
                if(inPreference == TempUnits.kCelsius)
                    return FahrenheitToCelsius(inBaseValue);
                break;
            case kCelsius:
                if(inPreference == TempUnits.kFahrenheit)
                    return CelsiusToFahrenheit(inBaseValue);
                break;
        }

        return null;
    }



    public Number GetSpeedInPreferredUnits(Number inBaseValue, DefaultUnits inDefaultUnits, SpeedUnits inPreference)
    {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultSpeedUnit() == null || inPreference == null)
            return null;

        if (inPreference == inDefaultUnits.GetDefaultSpeedUnit())
            return inBaseValue;

        switch (inDefaultUnits.GetDefaultSpeedUnit())
        {
            case kMps:
                if(inPreference == SpeedUnits.kMph)
                    return MpsToMph(inBaseValue);
                if(inPreference == SpeedUnits.kKmph)
                    return MpsToKmph(inBaseValue);
                break;
            case kMph:
                if(inPreference == SpeedUnits.kMps)
                    return MphToMps(inBaseValue);
                if(inPreference == SpeedUnits.kKmph)
                    return MphToKmph(inBaseValue);
                break;
            case kKmph:
                if(inPreference == SpeedUnits.kMps)
                    return KmphToMps(inBaseValue);
                if(inPreference == SpeedUnits.kMph)
                    return KmphToMph(inBaseValue);
                break;
        }

        return null;
    }



    public Number GetLengthInPreferredUnits(Number inBaseValue, DefaultUnits inDefaultUnits, LengthUnits inPreference)
    {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultTempUnit() == null || inPreference == null)
            return null;

        if (inPreference == inDefaultUnits.GetDefaultLengthUnit())
            return inBaseValue;

        switch (inDefaultUnits.GetDefaultLengthUnit())
        {
            case kIn:
                if(inPreference == LengthUnits.kMm)
                    return InToMm(inBaseValue);
                break;
            case kMm:
                if(inPreference == LengthUnits.kIn)
                    return MmToIn(inBaseValue);
                break;
        }

        return null;
    }



    public Number GetPressureInPreferredUnits(Number inBaseValue, DefaultUnits inDefaultUnits, PressureUnits inPreference)
    {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultTempUnit() == null || inPreference == null)
            return null;

        if (inPreference == inDefaultUnits.GetDefaultPressureUnit())
            return inBaseValue;

        switch (inDefaultUnits.GetDefaultPressureUnit())
        {
            case kInHg:
                if(inPreference == PressureUnits.kMmHg)
                    return InToMm(inBaseValue);
                break;
            case kMmHg:
                if(inPreference == PressureUnits.kInHg)
                    return MmToIn(inBaseValue);
                break;
        }

        return null;
    }



    public CompassDirections GetCompassDirection(Number inDirection)
    {
        if(inDirection == null)
            return null;

        double direction = inDirection.doubleValue();

        while(direction < 0)
            direction += 360;

        while (direction >= 360)
            direction -= 360;

        if(direction >= 0 && direction <= 11.25)
            return CompassDirections.N;
        if(direction > 11.25 && direction <= 33.75)
            return CompassDirections.NNE;
        if(direction > 33.75 && direction <= 56.25)
            return CompassDirections.NE;
        if(direction > 56.25 && direction <= 78.75)
            return CompassDirections.ENE;
        if(direction > 78.25 && direction <= 101.25)
            return CompassDirections.E;
        if(direction > 101.25 && direction <= 123.75)
            return CompassDirections.ESE;
        if(direction > 123.75 && direction <= 146.25)
            return CompassDirections.SE;
        if(direction > 146.25 && direction <= 168.75)
            return CompassDirections.SSE;
        if(direction > 168.75 && direction <= 191.25)
            return CompassDirections.S;
        if(direction > 191.25 && direction <= 213.75)
            return CompassDirections.SSW;
        if(direction > 213.75 && direction <= 236.25)
            return CompassDirections.SW;
        if(direction > 236.25 && direction <= 258.75)
            return CompassDirections.WSW;
        if(direction > 258.75 && direction <= 281.25)
            return CompassDirections.W;
        if(direction > 281.25 && direction <= 303.75)
            return CompassDirections.WNW;
        if(direction > 303.75 && direction <= 326.25)
            return CompassDirections.NW;
        if(direction > 326.25 && direction <= 348.75)
            return CompassDirections.NNW;
        if(direction > 348.75 && direction <= 360)
            return CompassDirections.N;

        return null;
    }
}
