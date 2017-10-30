package org.mesonet.app.formulas;



public class UnitConverter
{
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



    public Number MpsToKmph(Number inMps)
    {
        if(inMps == null)
            return null;

        return inMps.doubleValue() * 3.6;
    }



    public Number MmToIn(Number inMm)
    {
        if(inMm == null)
            return null;

        return inMm.doubleValue() * 0.0393700787401575;
    }



    public Number MmHgToInHg(Number inMmHg)
    {
        if(inMmHg == null)
            return null;

        return inMmHg.doubleValue() * (1 / 25.4);
    }
}
