package org.mesonet.app.site.mesonetdata;


import org.mesonet.app.formulas.UnitConverter;

public class DerivedValues
{
    private static final double kWindChillTempThreshold = 50.0;
    private static final double kHeatIndexTempThreshold = 80.0;

    private static DerivedValues sDerivedValues;


    public static DerivedValues GetInstance() {
        if (sDerivedValues == null)
            sDerivedValues = new DerivedValues();

        return sDerivedValues;
    }


    public Number GetHeatIndex(Number inTemp, Number inHumidity) {
        if (inTemp == null || inHumidity == null)
            return null;

        double fahrenheit = UnitConverter.GetInstance().CelsiusToFahrenheit(inTemp).doubleValue();

        if (fahrenheit < kHeatIndexTempThreshold)
            return null;

        return UnitConverter.GetInstance().FahrenheitToCelsius(-42.379 +
                2.04901523 * fahrenheit +
                10.14333127 * inHumidity.doubleValue() -
                0.22475541 * fahrenheit * inHumidity.doubleValue() -
                0.00683783 * Math.pow(fahrenheit, 2) -
                0.05481717 * Math.pow(inHumidity.doubleValue(), 2) +
                0.00122874 * Math.pow(fahrenheit, 2) * inHumidity.doubleValue() +
                0.00085282 * fahrenheit * Math.pow(inHumidity.doubleValue(), 2) -
                0.00000199 * Math.pow(fahrenheit, 2) * Math.pow(inHumidity.doubleValue(), 2));
    }


    public Number GetWindChill(Number inTemp, Number inWindSpeed) {
        if (inTemp == null || inWindSpeed == null)
            return null;

        double fahrenheit = UnitConverter.GetInstance().CelsiusToFahrenheit(inTemp).doubleValue();
        double mph = UnitConverter.GetInstance().MpsToMph(inWindSpeed).doubleValue();

        if (fahrenheit > kWindChillTempThreshold || mph < 3.0)
            return null;

        double windSpeedFunction = Math.pow(mph, 0.16);

        return UnitConverter.GetInstance().FahrenheitToCelsius(35.74 + 0.6215 * fahrenheit - 35.75 * windSpeedFunction + 0.4275 * fahrenheit * windSpeedFunction);
    }



    public Number GetApparentTemperature(Number inTemp, Number inWindSpeed, Number inHumidity)
    {
        if(inTemp == null)
            return null;

        double fahrenheit = UnitConverter.GetInstance().CelsiusToFahrenheit(inTemp).doubleValue();

        if(fahrenheit <= kWindChillTempThreshold)
        {
            if(inWindSpeed == null)
                return null;

            Number windChill = GetWindChill(inTemp, inWindSpeed);

            if(windChill != null)
                return windChill;
        }

        if(fahrenheit >= kHeatIndexTempThreshold)
        {
            if(inHumidity == null)
                return null;

            Number heatIndex = GetHeatIndex(inTemp, inHumidity);

            if(heatIndex != null)
                return heatIndex;
        }

        return inTemp;
    }



    public Number GetDewPoint(Number inTemperature, Number inHumidity)
    {
        if(inTemperature == null || inHumidity == null)
            return null;

        double saturatedPressure = 6.11 * Math.pow(10, ((7.5 * inTemperature.doubleValue()) / (237.3 + inTemperature.doubleValue())));

        return (237.3 * Math.log((saturatedPressure * inHumidity.doubleValue()) / 611.0)) / (7.5 * Math.log(10) - Math.log((saturatedPressure * inHumidity.doubleValue()) / 611.0));
    }
}
