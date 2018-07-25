package org.mesonet.dataprocessing.userdata

import android.content.Context
import io.reactivex.subjects.BehaviorSubject


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun UnitPreferencesSubject(inContext: Context): BehaviorSubject<UnitPreference>
    fun SetUnitPreference(inContext: Context, inPreference: UnitPreference)
    fun SelectedStidSubject(inContext: Context): BehaviorSubject<String>
    fun SetSelectedStid(inContext: Context, inStid: String)
    fun SelectedRadarSubject(inContext: Context): BehaviorSubject<String>
    fun SetSelectedRadar(inContext: Context, inRadarName: String)
    fun Dispose()
}