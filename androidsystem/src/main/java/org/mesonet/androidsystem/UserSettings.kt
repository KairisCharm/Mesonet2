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
class UserSettings @Inject constructor(var mContext: Context) : Preferences {
    val mUnitPreferenceSubject: BehaviorSubject<Preferences.UnitPreference> = BehaviorSubject.create()

    val mStidSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val mRadarSubject: BehaviorSubject<String> = BehaviorSubject.create()
    init {
        mUnitPreferenceSubject.onNext(Preferences.UnitPreference.valueOf(GetStringPreference(kUnitPreference, Preferences.UnitPreference.kImperial.name)))
        mStidSubject.onNext(GetStringPreference(UserSettings.kSelectedStid, ""))
        mRadarSubject.onNext(GetStringPreference(UserSettings.kSelectedRadar, ""))
    }

    internal fun SetPreference(inName: String, inValue: String) {
        Observable.create (ObservableOnSubscribe<Void>{
            synchronized(UserSettings@this)
            {
                val preferences = mContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

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

    override fun SelectedStidSubject(): BehaviorSubject<String> {
        return mStidSubject
    }

    override fun SetSelectedStid(inStid: String) {
        Observable.create(ObservableOnSubscribe<Void>{
            SetPreference(UserSettings.kSelectedStid, inStid)
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

    override fun SelectedRadarSubject(): BehaviorSubject<String> {
        return mRadarSubject
    }

    override fun SetSelectedRadar(inRadarName: String) {
        Observable.create(ObservableOnSubscribe<Void> {
            SetPreference(UserSettings.kSelectedRadar, inRadarName)
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