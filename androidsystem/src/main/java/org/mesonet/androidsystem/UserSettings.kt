package org.mesonet.androidsystem

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.mesonet.dataprocessing.userdata.Preferences

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserSettings @Inject constructor(var mContext: Context) : Preferences {

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


    override fun UnitPreferencesObservable(): Observable<Preferences.UnitPreference> {
        return Observable.create(ObservableOnSubscribe<Preferences.UnitPreference>{
            it.onNext(Preferences.UnitPreference.valueOf(GetStringPreference(UserSettings.kUnitPreference, Preferences.UnitPreference.kImperial.name)))
        }).subscribeOn(Schedulers.computation())
    }

    override fun SetUnitPreference(inPreference: Preferences.UnitPreference) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(UserSettings.kUnitPreference, inPreference.toString())
        }).subscribeOn(Schedulers.computation()).subscribe()
    }

    override fun SelectedStidObservable(): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String>{
            it.onNext(GetStringPreference(UserSettings.kSelectedStid, "nrmn"))
        }).subscribeOn(Schedulers.computation())
    }

    override fun SetSelectedStid(inStid: String) {
        Observable.create(ObservableOnSubscribe<Void>{
            SetPreference(UserSettings.kSelectedStid, inStid)
        }).subscribeOn(Schedulers.computation()).subscribe()
    }

    override fun SelectedRadarObservable(): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String> {
            it.onNext(GetStringPreference(UserSettings.kSelectedRadar, "KTLX"))
        }).subscribeOn(Schedulers.computation())
    }

    override fun SetSelectedRadar(inRadarName: String) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(UserSettings.kSelectedRadar, inRadarName)
        }).subscribeOn(Schedulers.computation()).subscribe()
    }


    companion object {
        private val kMesonetSettings = "MesonetSettings"
        val kUnitPreference = "UnitPreference"
        val kSelectedStid = "SelectedStid"
        val kSelectedRadar = "SelectedRadar"
    }
}