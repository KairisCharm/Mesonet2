package org.mesonet.dataprocessing.formulas


import javax.inject.Inject

class MathMethods @Inject
internal constructor() {


    fun Round(inBase: Number?, inDecimals: Int): Number? {
        if (inBase == null)
            return null

        val tenTimesDecimals = Math.pow(10.0, inDecimals.toDouble())

        return Math.round(inBase.toDouble() * tenTimesDecimals) / tenTimesDecimals
    }
}
