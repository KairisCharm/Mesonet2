package org.mesonet.dataprocessing.site.mesonetdata

import org.mesonet.dataprocessing.formulas.UnitConverter
import java.util.Date


interface MesonetData {
    fun GetTemp(): Double?
    fun GetApparentTemp(): Double?
    fun GetDewpoint(): Double?
    fun GetWindSpd(): Double?
    fun GetWindDirection(): UnitConverter.CompassDirections?
    fun GetMaxWind(): Double?
    fun Get24HrRain(): Double?
    fun GetHumidity(): Int?
    fun GetPressure(): Double?
    fun GetTime(): Date?
}
