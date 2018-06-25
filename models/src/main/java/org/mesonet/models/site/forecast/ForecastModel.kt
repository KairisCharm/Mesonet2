package org.mesonet.models.site.forecast

import org.mesonet.core.DefaultUnits

class ForecastModel : Forecast
{
    internal var mTime: String? = null
    internal var mIconUrl: String? = null
    internal var mStatus: String? = null
    internal var mHighOrLow: Forecast.HighOrLow? = null
    internal var mTemp: Number? = null
    internal var mWindDirectionStart: DefaultUnits.CompassDirections? = null
    internal var mWindDirectionEnd: DefaultUnits.CompassDirections? = null
    internal var mWindMin: Number? = null
    internal var mWindMax: Number? = null

    override fun GetTime(): String? {
        return mTime
    }

    override fun GetTemp(): Number? {
        return mTemp
    }

    override fun GetIconId(): String? {
        return mIconUrl?.removePrefix("http://www.nws.noaa.gov/weather/images/fcicons")?.removeSuffix(".jpg")
    }

    override fun GetStatus(): String? {
        return mStatus
    }

    override fun GetHighOrLow(): Forecast.HighOrLow? {
        return mHighOrLow
    }

    override fun GetWindMin(): Number? {
        return mWindMin
    }

    override fun GetWindMax(): Number? {
        return mWindMax
    }

    override fun GetWindDirectionStart(): DefaultUnits.CompassDirections? {
        return mWindDirectionStart
    }

    override fun GetWindDirectionEnd(): DefaultUnits.CompassDirections? {
        return mWindDirectionEnd
    }

    override fun GetDefaultTempUnit(): DefaultUnits.TempUnits {
        return DefaultUnits.TempUnits.kFahrenheit
    }

    override fun GetDefaultLengthUnit(): DefaultUnits.LengthUnits? {
        return null
    }

    override fun GetDefaultSpeedUnit(): DefaultUnits.SpeedUnits {
        return DefaultUnits.SpeedUnits.kMph
    }

    override fun GetDefaultPressureUnit(): DefaultUnits.PressureUnits? {
        return null
    }

}
