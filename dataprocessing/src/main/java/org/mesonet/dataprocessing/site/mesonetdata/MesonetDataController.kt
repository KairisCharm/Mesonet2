package org.mesonet.dataprocessing.site.mesonetdata

import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.DefaultUnits
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.mesonetdata.MesonetData
import java.util.*

interface MesonetDataController
{
    fun GetDataSubject(): BehaviorSubject<MesonetData>
    fun GetStationName(): String
    fun ProcessTime(): Date?
    fun ProcessTemp(inUnitPreference: Preferences.UnitPreference): Double?
    fun ProcessApparentTemp(inUnitPreference: Preferences.UnitPreference): Double?
    fun ProcessDewpoint(inUnitPreference: Preferences.UnitPreference): Double?
    fun ProcessWindSpd(inUnitPreference: Preferences.UnitPreference): Double?
    fun ProcessWindDirection(): DefaultUnits.CompassDirections?
    fun Process24HrRain(inUnitPreference: Preferences.UnitPreference): Double?
    fun ProcessHumidity(): Int?
    fun ProcessMaxWind(inUnitPreference: Preferences.UnitPreference): Double?
    fun ProcessPressure(inUnitPreference: Preferences.UnitPreference): Double?
    fun Dispose()
}