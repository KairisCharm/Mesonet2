package org.mesonet.androidsystem

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
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

    internal fun SetPreference(inContext: Context, inName: String, inValue: String) {
        Observable.create (ObservableOnSubscribe<Void>{
            synchronized(UserSettings@this)
            {
                val preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

                val editor = preferences.edit()
                editor.putString(inName, inValue)
                editor.apply()

                it.onComplete()
            }
        }).subscribe(object: Observer<Void> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }


    internal fun GetStringPreference(inContext: Context, inName: String, inDefault: String): String {
        synchronized(UserSettings@this)
        {
            val preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

            return preferences.getString(inName, inDefault)
        }
    }


    override fun UnitPreferencesSubject(inContext: Context): BehaviorSubject<Preferences.UnitPreference> {
        val prefence = Preferences.UnitPreference.valueOf(GetStringPreference(inContext, kUnitPreference, Preferences.UnitPreference.kImperial.name))
        if(!mUnitPreferenceSubject.hasValue() || mUnitPreferenceSubject.value != prefence)
            mUnitPreferenceSubject.onNext(prefence)

        return mUnitPreferenceSubject
    }


    override fun SetUnitPreference(inContext: Context, inPreference: Preferences.UnitPreference) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(inContext, UserSettings.kUnitPreference, inPreference.toString())
            mUnitPreferenceSubject.onNext(inPreference)
            it.onComplete()
        }).subscribeOn(Schedulers.computation()).subscribe(object: Observer<Void> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }

    override fun SelectedStidSubject(inContext: Context): BehaviorSubject<String> {
        val preference = GetStringPreference(inContext, UserSettings.kSelectedStid, "")
        if(!mStidSubject.hasValue() || mStidSubject.value != preference)
            mStidSubject.onNext(preference)

        return mStidSubject
    }

    override fun SetSelectedStid(inContext: Context, inStid: String) {
        Observable.create(ObservableOnSubscribe<Void>{
            SetPreference(inContext, UserSettings.kSelectedStid, inStid)
            mStidSubject.onNext(inStid)
            it.onComplete()
        }).subscribeOn(Schedulers.computation()).subscribe(object: Observer<Void> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }

    override fun SelectedRadarSubject(inContext: Context): BehaviorSubject<String> {
        val preference = GetStringPreference(inContext, UserSettings.kSelectedRadar, "")
        if(!mRadarSubject.hasValue() || mRadarSubject.value != preference)
            mRadarSubject.onNext(preference)

        return mRadarSubject
    }

    override fun SetSelectedRadar(inContext: Context, inRadarName: String) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(inContext, UserSettings.kSelectedRadar, inRadarName)
            mRadarSubject.onNext(inRadarName)
            it.onComplete()
        }).subscribeOn(Schedulers.computation()).subscribe(object: Observer<Void> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
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