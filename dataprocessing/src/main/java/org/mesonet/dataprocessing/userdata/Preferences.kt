package org.mesonet.dataprocessing.userdata

import io.reactivex.subjects.BehaviorSubject


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun UnitPreferencesSubject(): BehaviorSubject<UnitPreference>
    fun SetUnitPreference(inPreference: UnitPreference)
    fun SelectedStidSubject(): BehaviorSubject<String>
    fun SetSelectedStid(inStid: String)
    fun SelectedRadarSubject(): BehaviorSubject<String>
    fun SetSelectedRadar(inRadarName: String)
}