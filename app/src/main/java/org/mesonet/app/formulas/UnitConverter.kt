package org.mesonet.app.formulas


import javax.inject.Inject

class UnitConverter @Inject
internal constructor() {
    enum class TempUnits {
        kCelsius, kFahrenheit
    }

    enum class SpeedUnits {
        kMps, kKmph, kMph
    }

    enum class LengthUnits {
        kMm, kIn
    }

    enum class PressureUnits {
        kMb, kInHg
    }

    enum class CompassDirections {
        N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW
    }


    fun CelsiusToFahrenheit(inCelsius: Number?): Number? {
        return if (inCelsius == null) null else inCelsius.toDouble() * (9.0 / 5.0) + 32.0

    }


    fun FahrenheitToCelsius(inFahrenheit: Number?): Number? {
        return if (inFahrenheit == null) null else (inFahrenheit.toDouble() - 32.0) / (9.0 / 5.0)

    }


    fun MpsToMph(inMps: Number?): Number? {
        return if (inMps == null) null else inMps.toDouble() * 2.236936292054402

    }


    fun MphToMps(inMph: Number?): Number? {
        return if (inMph == null) null else inMph.toDouble() * (1.0 / MpsToMph(1)!!.toDouble())

    }


    fun MpsToKmph(inMps: Number?): Number? {
        return if (inMps == null) null else inMps.toDouble() * 3.6

    }


    fun KmphToMps(inKmph: Number?): Number? {
        return if (inKmph == null) null else inKmph.toDouble() * (1.0 / MpsToKmph(1)!!.toDouble())

    }


    fun MphToKmph(inMph: Number?): Number? {
        return if (inMph == null) null else inMph.toDouble() * 1.609344

    }


    fun KmphToMph(inMph: Number?): Number? {
        return if (inMph == null) null else inMph.toDouble() * (1.0 / MphToKmph(1)!!.toDouble())

    }


    fun MmToIn(inMm: Number?): Number? {
        return if (inMm == null) null else inMm.toDouble() * 0.0393700787401575

    }


    fun InToMm(inIn: Number?): Number? {
        return if (inIn == null) null else inIn.toDouble() * (1.0 / MmToIn(1)!!.toDouble())

    }


    fun InHgToMb(inIn: Number?): Number? {
        return if (inIn == null) null else inIn.toDouble() * 33.8639

    }


    fun MbToInHg(inIn: Number?): Number? {
        return if (inIn == null) null else inIn.toDouble() / 33.8639

    }


    fun GetTempInPreferredUnits(inBaseValue: Number?, inDefaultUnits: DefaultUnits?, inPreference: TempUnits?): Number? {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultTempUnit() == null || inPreference == null)
            return null

        if (inPreference == inDefaultUnits.GetDefaultTempUnit())
            return inBaseValue

        when (inDefaultUnits.GetDefaultTempUnit()) {
            TempUnits.kFahrenheit -> if (inPreference == TempUnits.kCelsius)
                return FahrenheitToCelsius(inBaseValue)
            TempUnits.kCelsius -> if (inPreference == TempUnits.kFahrenheit)
                return CelsiusToFahrenheit(inBaseValue)
        }

        return null
    }


    fun GetSpeedInPreferredUnits(inBaseValue: Number?, inDefaultUnits: DefaultUnits?, inPreference: SpeedUnits?): Number? {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultSpeedUnit() == null || inPreference == null)
            return null

        if (inPreference == inDefaultUnits.GetDefaultSpeedUnit())
            return inBaseValue

        when (inDefaultUnits.GetDefaultSpeedUnit()) {
            SpeedUnits.kMps -> {
                if (inPreference == SpeedUnits.kMph)
                    return MpsToMph(inBaseValue)
                if (inPreference == SpeedUnits.kKmph)
                    return MpsToKmph(inBaseValue)
            }
            SpeedUnits.kMph -> {
                if (inPreference == SpeedUnits.kMps)
                    return MphToMps(inBaseValue)
                if (inPreference == SpeedUnits.kKmph)
                    return MphToKmph(inBaseValue)
            }
            SpeedUnits.kKmph -> {
                if (inPreference == SpeedUnits.kMps)
                    return KmphToMps(inBaseValue)
                if (inPreference == SpeedUnits.kMph)
                    return KmphToMph(inBaseValue)
            }
        }

        return null
    }


    fun GetLengthInPreferredUnits(inBaseValue: Number?, inDefaultUnits: DefaultUnits?, inPreference: LengthUnits?): Number? {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultTempUnit() == null || inPreference == null)
            return null

        if (inPreference == inDefaultUnits.GetDefaultLengthUnit())
            return inBaseValue

        when (inDefaultUnits.GetDefaultLengthUnit()) {
            LengthUnits.kIn -> if (inPreference == LengthUnits.kMm)
                return InToMm(inBaseValue)
            LengthUnits.kMm -> if (inPreference == LengthUnits.kIn)
                return MmToIn(inBaseValue)
        }

        return null
    }


    fun GetPressureInPreferredUnits(inBaseValue: Number?, inDefaultUnits: DefaultUnits?, inPreference: PressureUnits?): Number? {
        if (inBaseValue == null || inDefaultUnits == null || inDefaultUnits.GetDefaultTempUnit() == null || inPreference == null)
            return null

        if (inPreference == inDefaultUnits.GetDefaultPressureUnit())
            return inBaseValue

        when (inDefaultUnits.GetDefaultPressureUnit()) {
            PressureUnits.kInHg -> if (inPreference == PressureUnits.kMb)
                return InHgToMb(inBaseValue)
            PressureUnits.kMb -> if (inPreference == PressureUnits.kInHg)
                return MbToInHg(inBaseValue)
        }

        return null
    }


    fun GetCompassDirection(inDirection: Number?): CompassDirections? {
        if (inDirection == null)
            return null

        var direction = inDirection.toDouble()

        while (direction < 0)
            direction += 360.0

        while (direction >= 360)
            direction -= 360.0

        if (direction >= 0 && direction <= 11.25)
            return CompassDirections.N
        if (direction > 11.25 && direction <= 33.75)
            return CompassDirections.NNE
        if (direction > 33.75 && direction <= 56.25)
            return CompassDirections.NE
        if (direction > 56.25 && direction <= 78.75)
            return CompassDirections.ENE
        if (direction > 78.25 && direction <= 101.25)
            return CompassDirections.E
        if (direction > 101.25 && direction <= 123.75)
            return CompassDirections.ESE
        if (direction > 123.75 && direction <= 146.25)
            return CompassDirections.SE
        if (direction > 146.25 && direction <= 168.75)
            return CompassDirections.SSE
        if (direction > 168.75 && direction <= 191.25)
            return CompassDirections.S
        if (direction > 191.25 && direction <= 213.75)
            return CompassDirections.SSW
        if (direction > 213.75 && direction <= 236.25)
            return CompassDirections.SW
        if (direction > 236.25 && direction <= 258.75)
            return CompassDirections.WSW
        if (direction > 258.75 && direction <= 281.25)
            return CompassDirections.W
        if (direction > 281.25 && direction <= 303.75)
            return CompassDirections.WNW
        if (direction > 303.75 && direction <= 326.25)
            return CompassDirections.NW
        if (direction > 326.25 && direction <= 348.75)
            return CompassDirections.NNW
        return if (direction > 348.75 && direction <= 360) CompassDirections.N else null

    }
}
