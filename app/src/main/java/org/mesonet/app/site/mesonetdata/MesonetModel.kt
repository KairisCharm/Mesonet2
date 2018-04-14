package org.mesonet.app.site.mesonetdata

import org.mesonet.app.formulas.DefaultUnits
import org.mesonet.app.formulas.UnitConverter


class MesonetModel : DefaultUnits {
    internal var STNM: Int? = null
    internal var TIME: Long? = null
    internal var RELH: Number? = null
    internal var TAIR: Number? = null
    internal var WSPD: Number? = null
    internal var WVEC: Number? = null
    internal var WDIR: Number? = null
    internal var WDSD: Number? = null
    internal var WSSD: Number? = null
    internal var WMAX: Number? = null
    internal var RAIN: Number? = null
    internal var PRES: Number? = null
    internal var SRAD: Number? = null
    internal var TA9M: Number? = null
    internal var WS2M: Number? = null
    internal var TS10: Number? = null
    internal var TB10: Number? = null
    internal var TS05: Number? = null
    internal var TS25: Number? = null
    internal var TS60: Number? = null
    internal var TR05: Number? = null
    internal var TR25: Number? = null
    internal var TR60: Number? = null
    internal var RAIN_24H: Number? = null
    internal var RAIN_24: Number? = null
    internal var STID: String? = null
    internal var YEAR: Int? = null
    internal var MONTH: Int? = null
    internal var DAY: Int? = null
    internal var HOUR: Int? = null
    internal var MINUTE: Int? = null
    internal var SECOND: Int? = null


    override fun GetDefaultTempUnit(): UnitConverter.TempUnits {
        return UnitConverter.TempUnits.kCelsius
    }


    override fun GetDefaultLengthUnit(): UnitConverter.LengthUnits {
        return UnitConverter.LengthUnits.kMm
    }


    override fun GetDefaultSpeedUnit(): UnitConverter.SpeedUnits {
        return UnitConverter.SpeedUnits.kMps
    }


    override fun GetDefaultPressureUnit(): UnitConverter.PressureUnits {
        return UnitConverter.PressureUnits.kMb
    }
}