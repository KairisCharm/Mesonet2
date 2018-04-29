package org.mesonet.dataprocessing.site.forecast

import org.mesonet.dataprocessing.formulas.DefaultUnits
import org.mesonet.dataprocessing.formulas.UnitConverter

class ForecastModel : DefaultUnits {
    internal var mTime: String? = null
    internal var mIconUrl: String? = null
    internal var mStatus: String? = null
    internal var mHighOrLow: HighOrLow? = null
    internal var mTemp: Number? = null
    internal var mWindDirection: UnitConverter.CompassDirections? = null
    internal var mWindGustsDirection: UnitConverter.CompassDirections? = null
    internal var mWindSpd: Number? = null
    internal var mWindGusts: Number? = null

    override fun GetDefaultTempUnit(): UnitConverter.TempUnits {
        return UnitConverter.TempUnits.kFahrenheit
    }

    override fun GetDefaultLengthUnit(): UnitConverter.LengthUnits? {
        return null
    }

    override fun GetDefaultSpeedUnit(): UnitConverter.SpeedUnits {
        return UnitConverter.SpeedUnits.kMps
    }

    override fun GetDefaultPressureUnit(): UnitConverter.PressureUnits? {
        return null
    }

    enum class HighOrLow {
        High, Low
    }
}
