package org.mesonet.dataprocessing.userdata

import io.reactivex.Observable


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun SetUnitPreference(inPreference: UnitPreference)
    fun GetSelectedStid()
    fun SetSelectedStid(inStid: String)
    fun GetSelectedRadar()
    fun SetSelectedRadar(inRadarName: String)
    fun GetUnitPreferencesObservable(): Observable<UnitPreference>
}