package org.mesonet.app.site.mesonetdata


import org.mesonet.app.formulas.UnitConverter
import javax.inject.Inject

class DerivedValues @Inject
constructor() {


    @Inject
    lateinit var mUnitConverter: UnitConverter


    fun GetHeatIndex(inTemp: Number?, inHumidity: Number?): Number? {
        if (inTemp == null || inHumidity == null)
            return null

        val fahrenheit = mUnitConverter.CelsiusToFahrenheit(inTemp)

        return if (fahrenheit?.toDouble()!! < kHeatIndexTempThreshold) null else mUnitConverter.FahrenheitToCelsius(-42.379 +
                2.04901523 * fahrenheit.toDouble() +
                10.14333127 * inHumidity.toDouble() -
                0.22475541 * fahrenheit.toDouble() * inHumidity.toDouble() -
                0.00683783 * Math.pow(fahrenheit.toDouble(), 2.0) -
                0.05481717 * Math.pow(inHumidity.toDouble(), 2.0) +
                0.00122874 * Math.pow(fahrenheit.toDouble(), 2.0) * inHumidity.toDouble() +
                0.00085282 * fahrenheit.toDouble() * Math.pow(inHumidity.toDouble(), 2.0) - 0.00000199 * Math.pow(fahrenheit.toDouble(), 2.0) * Math.pow(inHumidity.toDouble(), 2.0))

    }


    fun GetWindChill(inTemp: Number?, inWindSpeed: Number?): Number? {
        if (inTemp == null || inWindSpeed == null)
            return null

        val fahrenheit = mUnitConverter.CelsiusToFahrenheit(inTemp)
        val mph = mUnitConverter.MpsToMph(inWindSpeed)

        if (fahrenheit?.toDouble()!! > kWindChillTempThreshold || mph?.toDouble()!! < 3.0)
            return null

        val windSpeedFunction = Math.pow(mph.toDouble(), 0.16)

        return mUnitConverter.FahrenheitToCelsius(35.74 + 0.6215 * fahrenheit.toDouble() - 35.75 * windSpeedFunction + 0.4275 * fahrenheit.toDouble() * windSpeedFunction)
    }


    fun GetApparentTemperature(inTemp: Number?, inWindSpeed: Number?, inHumidity: Number?): Number? {
        if (inTemp == null)
            return null

        val fahrenheit = mUnitConverter.CelsiusToFahrenheit(inTemp)

        if (fahrenheit?.toDouble()!! <= kWindChillTempThreshold) {
            if (inWindSpeed == null)
                return null

            val windChill = GetWindChill(inTemp, inWindSpeed)

            if (windChill != null)
                return windChill
        }

        if (fahrenheit.toDouble() >= kHeatIndexTempThreshold) {
            if (inHumidity == null)
                return null

            val heatIndex = GetHeatIndex(inTemp, inHumidity)

            if (heatIndex != null)
                return heatIndex
        }

        return inTemp
    }


    fun GetDewpoint(inTemperature: Number?, inHumidity: Number?): Number? {
        if (inTemperature == null || inHumidity == null)
            return null

        val saturatedPressure = 6.11 * Math.pow(10.0, 7.5 * inTemperature.toDouble() / (237.3 + inTemperature.toDouble()))

        return 237.3 * Math.log(saturatedPressure * inHumidity.toDouble() / 611.0) / (7.5 * Math.log(10.0) - Math.log(saturatedPressure * inHumidity.toDouble() / 611.0))
    }


    fun GetMSLPressure(inTemperature: Number, inStationPressure: Number?, inElevation: Number?): Number? {
        return if (inStationPressure == null || inElevation == null) null else inStationPressure.toDouble() * Math.pow(1 - 0.0065 * inElevation.toDouble() / (inTemperature.toDouble() + 0.0065 * inElevation.toDouble() + 273.0), -5.257)

    }

    companion object {
        private val kWindChillTempThreshold = 50.0
        private val kHeatIndexTempThreshold = 80.0
    }
}
