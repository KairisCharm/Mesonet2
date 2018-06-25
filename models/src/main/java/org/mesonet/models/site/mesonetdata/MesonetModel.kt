package org.mesonet.models.site.mesonetdata

import org.mesonet.core.DefaultUnits


class MesonetModel : MesonetData
{
    internal var TIME: Long? = null
    internal var RELH: Number? = null
    internal var TAIR: Number? = null
    internal var WSPD: Number? = null
    internal var WDIR: Number? = null
    internal var WMAX: Number? = null
    internal var PRES: Number? = null
    internal var RAIN_24H: Number? = null
    internal var STID: String? = null



    override fun GetTime(): Long? {
        return TIME
    }

    override fun GetRelH(): Number? {
        return RELH
    }

    override fun GetTAir(): Number? {
        return TAIR
    }

    override fun GetWSpd(): Number? {
        return WSPD
    }

    override fun GetWDir(): Number? {
        return WDIR
    }

    override fun GetWMax(): Number? {
        return WMAX
    }

    override fun GetPres(): Number? {
        return PRES
    }

    override fun GetRain24H(): Number? {
        return RAIN_24H
    }

    override fun GetStID(): String? {
        return STID
    }


    override fun GetDefaultTempUnit(): DefaultUnits.TempUnits {
        return DefaultUnits.TempUnits.kCelsius
    }


    override fun GetDefaultLengthUnit(): DefaultUnits.LengthUnits {
        return DefaultUnits.LengthUnits.kMm
    }


    override fun GetDefaultSpeedUnit(): DefaultUnits.SpeedUnits {
        return DefaultUnits.SpeedUnits.kMps
    }


    override fun GetDefaultPressureUnit(): DefaultUnits.PressureUnits {
        return DefaultUnits.PressureUnits.kMb
    }
}