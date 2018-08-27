package org.mesonet.dataprocessing.userdata

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun UnitPreferencesObservable(inContext: Context): Observable<UnitPreference>
    fun SetUnitPreference(inContext: Context, inPreference: UnitPreference): Single<Int>
    fun SelectedStidObservable(inContext: Context): Observable<String>
    fun SetSelectedStid(inContext: Context, inStid: String): Single<Int>
    fun SelectedRadarObservable(inContext: Context): Observable<String>
    fun SetSelectedRadar(inContext: Context, inRadarName: String): Single<Int>
    fun Dispose()
}