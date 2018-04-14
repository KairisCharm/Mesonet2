package org.mesonet.app.radar


import javax.inject.Inject

class VincentysFormulae @Inject
constructor() {


    fun DegreesToRadians(inDegrees: Double): Double {
        return inDegrees * (Math.PI / 180)
    }


    fun HaversineFunction(inRadians: Double): Double {
        return (1 - Math.cos(inRadians)) / 2
    }


    fun Converge(inLonDifference: Double, inLat1: Double, inLat2: Double): VincentyConvergenceData {
        val data = VincentyConvergenceData()
        data.λ = inLonDifference

        return Converge(data, inLonDifference, inLat1, inLat2, 0)
    }


    @Throws(IllegalArgumentException::class)
    fun Converge(inData: VincentyConvergenceData, inLonDifference: Double, inLat1: Double, inLat2: Double, inAttempt: Int): VincentyConvergenceData {
        if (inAttempt >= 1000)
            throw IllegalArgumentException("Failed to converge while calculating lat/lon distances")

        val tanU1 = (1 - kEarthFlattening) * Math.tan(inLat1)
        val cosU1 = 1 / Math.sqrt(1 + tanU1 * tanU1)
        val sinU1 = tanU1 * cosU1

        val tanU2 = (1 - kEarthFlattening) * Math.tan(inLat2)
        val cosU2 = 1 / Math.sqrt(1 + tanU2 * tanU2)
        val sinU2 = tanU2 * cosU2

        var iterationCheck = 0.0

        val antimeridian = Math.abs(inLonDifference) > Math.PI

        inData.sinλ = Math.sin(inData.λ)
        inData.cosλ = Math.cos(inData.λ)
        inData.sinSqσ = cosU2 * inData.sinλ * (cosU2 * inData.sinλ) + (cosU1 * sinU2 - sinU1 * cosU2 * inData.cosλ) * (cosU1 * sinU2 - sinU1 * cosU2 * inData.cosλ)
        if (inData.sinSqσ != 0.0) {
            inData.sinσ = Math.sqrt(inData.sinSqσ)
            inData.cosσ = sinU1 * sinU2 + cosU1 * cosU2 * inData.cosλ
            inData.σ = Math.atan2(inData.sinσ, inData.cosσ)
            inData.sinα = cosU1 * cosU2 * inData.sinλ / inData.sinσ
            inData.cosSqα = 1 - inData.sinα * inData.sinα
            inData.cos2σM = if (inData.cosSqα != 0.0) inData.cosσ - 2.0 * sinU1 * sinU2 / inData.cosSqα else 0.0 // equatorial line: cosSqα=0 (§6)
            inData.C = kEarthFlattening / 16 * inData.cosSqα * (4 + kEarthFlattening * (4 - 3 * inData.cosSqα))
            inData.λʹ = inData.λ
            inData.λ = inLonDifference + (1 - inData.C) * kEarthFlattening * inData.sinα * (inData.σ + inData.C * inData.sinσ * (inData.cos2σM + inData.C * inData.cosσ * (-1 + 2.0 * inData.cos2σM * inData.cos2σM)))
            iterationCheck = if (antimeridian) Math.abs(inData.λ) - Math.PI else Math.abs(inData.λ)
        }
        if (iterationCheck > Math.PI) throw IllegalArgumentException("Failed to converge while calculating lat/lon distances")

        return if (Math.abs(inData.λ - inData.λʹ) > 1e-12) Converge(inData, inLonDifference, inLat1, inLat2, inAttempt + 1) else inData

    }


    fun CalculateDistance(inConvergenceData: VincentyConvergenceData): Double {
        val uSq = inConvergenceData.cosSqα * (kSemiMajorAxis * kSemiMajorAxis - kSemiMinorAxis * kSemiMinorAxis) / (kSemiMinorAxis * kSemiMinorAxis)
        val A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)))
        val B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)))
        val Δσ = B * inConvergenceData.sinσ * (inConvergenceData.cos2σM + B / 4 * (inConvergenceData.cosσ * (-1 + 2.0 * inConvergenceData.cos2σM * inConvergenceData.cos2σM) - B / 6 * inConvergenceData.cos2σM * (-3 + 4.0 * inConvergenceData.sinσ * inConvergenceData.sinσ) * (-3 + 4.0 * inConvergenceData.cos2σM * inConvergenceData.cos2σM)))

        return kSemiMinorAxis * A * (inConvergenceData.σ - Δσ)
    }


    fun LatLonDegreesToDistance(inLat1: Double, inLat2: Double, inLon1: Double, inLon2: Double): Double {
        return LatLonRadiansToDistance(DegreesToRadians(inLat1), DegreesToRadians(inLat2), DegreesToRadians(inLon1), DegreesToRadians(inLon2))
    }


    fun LatLonRadiansToDistance(inLat1: Double, inLat2: Double, inLon1: Double, inLon2: Double): Double {
        val lonDifference = inLon2 - inLon1
        return CalculateDistance(Converge(lonDifference, inLat1, inLat2))
    }


    class VincentyConvergenceData {
        internal var sinλ: Double = 0.toDouble()
        internal var cosλ: Double = 0.toDouble()
        internal var sinSqσ: Double = 0.toDouble()
        internal var sinσ: Double = 0.toDouble()
        internal var cosσ: Double = 0.toDouble()
        internal var σ: Double = 0.toDouble()
        internal var sinα: Double = 0.toDouble()
        internal var cosSqα: Double = 0.toDouble()
        internal var cos2σM: Double = 0.toDouble()
        internal var C: Double = 0.toDouble()
        internal var λʹ: Double = 0.toDouble()
        internal var λ: Double = 0.toDouble()
    }

    companion object {
        val kSemiMajorAxis = 6378137.0
        val kSemiMinorAxis = 6356752.314245
        val kEarthFlattening = 1 / 298.257223563
    }
}
