package org.mesonet.androidsystem

import android.content.Context
import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.dataprocessing.userdata.PreferencesObservable
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserSettings @Inject constructor(var mThreadHandler: ThreadHandler, var mContext: Context) : PreferencesObservable(), Preferences {

    fun SetPreference(inName: String, inValue: String) {
        mThreadHandler.Run("AndroidSystem", Runnable {
            val preferences = mContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

            val editor = preferences.edit()
            editor.putString(inName, inValue)
            editor.apply()
        })
    }


    internal fun GetStringPreference(inName: String, inDefault: String) : String {
        val preferences = mContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

        return preferences.getString(inName, inDefault)
    }



    override fun GetUnitPreference(inListener: Preferences.UnitPreferenceListener)
    {
        var result = Preferences.UnitPreference.kImperial
        mThreadHandler.Run("AndroidSystem", Runnable {
            result = Preferences.UnitPreference.valueOf(GetStringPreference(UserSettings.kUnitPreference, Preferences.UnitPreference.kImperial.name))
        }, Runnable {
            inListener.UnitPreferenceFound(result)
        })
    }

    override fun SetUnitPreference(inPreference: Preferences.UnitPreference) {
        mThreadHandler.Run("AndroidSystem", Runnable {
            SetPreference(UserSettings.kUnitPreference, inPreference.toString())
        })
    }

    override fun GetSelectedStid(inListener: Preferences.StidListener) {
        var result = ""

        mThreadHandler.Run("AndroidSystem", Runnable {
            result = GetStringPreference(UserSettings.kSelectedStid, "nrmn")
        }, Runnable {
            inListener.StidFound(result)
        })
    }

    override fun SetSelectedStid(inStid: String) {
        mThreadHandler.Run("AndroidSystem", Runnable {
            SetPreference(UserSettings.kSelectedStid, inStid)
        })
    }

    override fun GetSelectedRadar(inListener: Preferences.RadarListener) {
        var result = "KTLX"

        mThreadHandler.Run("AndroidSystem", Runnable {
            result = GetStringPreference(UserSettings.kSelectedRadar, "KTLX")
        }, Runnable {
            inListener.RadarFound(result)
        })
    }

    override fun SetSelectedRadar(inRadarName: String) {
        mThreadHandler.Run("AndroidSystem", Runnable {
            SetPreference(UserSettings.kSelectedRadar, inRadarName)
        })
    }

    override fun GetPreferencesObservable(): PreferencesObservable {
        return this
    }

    companion object {
        private val kMesonetSettings = "MesonetSettings"
        val kUnitPreference = "UnitPreference"
        val kSelectedStid = "SelectedStid"
        val kSelectedRadar = "SelectedRadar"
    }
}
