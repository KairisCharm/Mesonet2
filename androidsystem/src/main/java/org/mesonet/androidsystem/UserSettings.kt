package org.mesonet.androidsystem

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.dataprocessing.userdata.Preferences

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserSettings @Inject constructor(var mContext: Context) : Preferences {

    val mUnitPreferenceSubject: BehaviorSubject<Preferences.UnitPreference> = BehaviorSubject.create()
    val mStidSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val mRadarSubject: BehaviorSubject<String> = BehaviorSubject.create()

    init {
        mUnitPreferenceSubject.onNext(Preferences.UnitPreference.valueOf(GetStringPreference(kUnitPreference, Preferences.UnitPreference.kImperial.name)))
        mStidSubject.onNext(GetStringPreference(UserSettings.kSelectedStid, "nrmn"))
        mRadarSubject.onNext(GetStringPreference(UserSettings.kSelectedRadar, "KTLX"))
    }


    internal fun SetPreference(inName: String, inValue: String) {
        Observable.create (ObservableOnSubscribe<Void>{
            synchronized(UserSettings@this)
            {
                val preferences = mContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

                val editor = preferences.edit()
                editor.putString(inName, inValue)
                editor.apply()
            }
        }).subscribe()
    }


    internal fun GetStringPreference(inName: String, inDefault: String): String {
        synchronized(UserSettings@this)
        {
            val preferences = mContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

            return preferences.getString(inName, inDefault)
        }
    }


    override fun UnitPreferencesSubject(): BehaviorSubject<Preferences.UnitPreference> {
        return mUnitPreferenceSubject
    }

    override fun SetUnitPreference(inPreference: Preferences.UnitPreference) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(UserSettings.kUnitPreference, inPreference.toString())
            mUnitPreferenceSubject.onNext(inPreference)
        }).subscribeOn(Schedulers.computation()).subscribe()
    }

    override fun SelectedStidSubject(): BehaviorSubject<String> {
        return mStidSubject
    }

    override fun SetSelectedStid(inStid: String) {
        Observable.create(ObservableOnSubscribe<Void>{
            SetPreference(UserSettings.kSelectedStid, inStid)
            mStidSubject.onNext(inStid)
        }).subscribeOn(Schedulers.computation()).subscribe()
    }

    override fun SelectedRadarSubject(): BehaviorSubject<String> {
        return mRadarSubject
    }

    override fun SetSelectedRadar(inRadarName: String) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(UserSettings.kSelectedRadar, inRadarName)
            mRadarSubject.onNext(inRadarName)
        }).subscribeOn(Schedulers.computation()).subscribe()
    }


    companion object {
        private val kMesonetSettings = "MesonetSettings"
        val kUnitPreference = "UnitPreference"
        val kSelectedStid = "SelectedStid"
        val kSelectedRadar = "SelectedRadar"
    }
}