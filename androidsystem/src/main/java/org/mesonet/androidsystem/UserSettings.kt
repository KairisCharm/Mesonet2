package org.mesonet.androidsystem

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerContext
import org.mesonet.dataprocessing.userdata.Preferences

import javax.inject.Inject


@PerContext
class UserSettings @Inject constructor() : Preferences {
    val mUnitPreferenceSubject: BehaviorSubject<Preferences.UnitPreference> = BehaviorSubject.create()

    val mStidSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val mRadarSubject: BehaviorSubject<String> = BehaviorSubject.create()

    internal fun SetPreference(inContext: Context, inName: String, inValue: String): Single<Int> {
        return Single.create (SingleOnSubscribe<Int>{
            synchronized(UserSettings@this)
            {
                val preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

                val editor = preferences.edit()
                editor.putString(inName, inValue)
                editor.apply()

                it.onSuccess(0)
            }
        })
    }


    internal fun GetStringPreference(inContext: Context, inName: String, inDefault: String): String {
        synchronized(UserSettings@this)
        {
            val preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

            return preferences.getString(inName, inDefault)?: ""
        }
    }


    override fun UnitPreferencesObservable(inContext: Context): Observable<Preferences.UnitPreference> {
        val prefence = Preferences.UnitPreference.valueOf(GetStringPreference(inContext, kUnitPreference, Preferences.UnitPreference.kImperial.name))
        if(!mUnitPreferenceSubject.hasValue() || mUnitPreferenceSubject.value != prefence)
            mUnitPreferenceSubject.onNext(prefence)

        return mUnitPreferenceSubject
    }


    override fun SetUnitPreference(inContext: Context, inPreference: Preferences.UnitPreference): Single<Int> {
        return SetPreference(inContext, UserSettings.kUnitPreference, inPreference.toString()).doOnSubscribe{it ->
            mUnitPreferenceSubject.onNext(inPreference)
        }.subscribeOn(Schedulers.computation())
    }

    override fun SelectedStidObservable(inContext: Context): Observable<String> {
        val preference = GetStringPreference(inContext, UserSettings.kSelectedStid, "")
        if(!mStidSubject.hasValue() || mStidSubject.value != preference)
            mStidSubject.onNext(preference)

        return mStidSubject
    }

    override fun SetSelectedStid(inContext: Context, inStid: String): Single<Int> {
        return SetPreference(inContext, UserSettings.kSelectedStid, inStid).doOnSubscribe {
            mStidSubject.onNext(inStid)
        }.subscribeOn(Schedulers.computation())
    }

    override fun SelectedRadarObservable(inContext: Context): Observable<String> {
        val preference = GetStringPreference(inContext, UserSettings.kSelectedRadar, "")
        if(!mRadarSubject.hasValue() || mRadarSubject.value != preference)
            mRadarSubject.onNext(preference)

        return mRadarSubject
    }

    override fun SetSelectedRadar(inContext: Context, inRadarName: String): Single<Int> {
        return SetPreference(inContext, UserSettings.kSelectedRadar, inRadarName).doOnSubscribe{
            mRadarSubject.onNext(inRadarName)
        }.subscribeOn(Schedulers.computation())
    }

    override fun Dispose() {
        mUnitPreferenceSubject.onComplete()
        mStidSubject.onComplete()
        mRadarSubject.onComplete()
    }


    companion object {
        private val kMesonetSettings = "MesonetSettings"
        val kUnitPreference = "UnitPreference"
        val kSelectedStid = "SelectedStid"
        val kSelectedRadar = "SelectedRadar"
    }
}