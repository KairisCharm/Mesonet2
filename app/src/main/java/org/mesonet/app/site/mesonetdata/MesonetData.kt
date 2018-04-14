package org.mesonet.app.site.mesonetdata

import org.mesonet.app.formulas.UnitConverter
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
