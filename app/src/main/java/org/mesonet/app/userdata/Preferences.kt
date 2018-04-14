package org.mesonet.app.userdata

import java.util.Observable


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun GetUnitPreference(): UnitPreference
    fun SetUnitPreference(inPreference: UnitPreference)
    fun GetSelectedStid(): String
    fun SetSelectedStid(inStid: String)
    fun GetSelectedRadar(): String
    fun SetSelectedRadar(inRadarName: String)
    fun GetPreferencesObservable(): PreferencesObservable
}