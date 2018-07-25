package org.mesonet.androidsystem

import android.app.Application
import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerActivity
import org.mesonet.dataprocessing.userdata.Preferences

import javax.inject.Inject


@PerActivity
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
        if(!mUnitPreferenceSubject.hasValue())
            mUnitPreferenceSubject.onNext(Preferences.UnitPreference.valueOf(GetStringPreference(inContext, kUnitPreference, Preferences.UnitPreference.kImperial.name)))

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
        if(!mStidSubject.hasValue())
            mStidSubject.onNext(GetStringPreference(inContext, UserSettings.kSelectedStid, ""))

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
        if(!mRadarSubject.hasValue())
            mStidSubject.onNext(GetStringPreference(inContext, UserSettings.kSelectedRadar, ""))

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