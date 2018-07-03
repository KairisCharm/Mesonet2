package org.mesonet.dataprocessing.userdata

import io.reactivex.Observable


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun UnitPreferencesObservable(): Observable<UnitPreference>
    fun SetUnitPreference(inPreference: UnitPreference)
    fun SelectedStidObservable(): Observable<String>
    fun SetSelectedStid(inStid: String)
    fun SelectedRadarObservable(): Observable<String>
    fun SetSelectedRadar(inRadarName: String)
}