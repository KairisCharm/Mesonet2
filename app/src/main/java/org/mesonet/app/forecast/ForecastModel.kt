package org.mesonet.app.forecast


import org.mesonet.app.formulas.DefaultUnits
import org.mesonet.app.formulas.UnitConverter

class ForecastModel : DefaultUnits {
    var mTime: String? = null
    var mIconUrl: String? = null
    var mStatus: String? = null
    var mHighOrLow: HighOrLow? = null
    var mTemp: Number? = null
    var mWindDirection: UnitConverter.CompassDirections? = null
    var mWindGustsDirection: UnitConverter.CompassDirections? = null
    var mWindSpd: Number? = null
    var mWindGusts: Number? = null
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
