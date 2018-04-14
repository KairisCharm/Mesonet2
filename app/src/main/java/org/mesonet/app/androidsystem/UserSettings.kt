package org.mesonet.app.androidsystem

import android.content.Context

import javax.inject.Inject


class UserSettings @Inject
internal constructor() {


    fun SetPreference(inContext: Context, inName: String, inValue: String) {
        val preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

        val editor = preferences.edit()
        editor.putString(inName, inValue)
        editor.apply()
    }


    fun GetStringPreference(inContext: Context, inName: String, inDefault: String): String {
        val preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE)

        return preferences.getString(inName, inDefault)
    }

    companion object {
        private val kMesonetSettings = "MesonetSettings"
        val kUnitPreference = "UnitPreference"
        val kSelectedStid = "SelectedStid"
        val kSelectedRadar = "SelectedRadar"
    }
}
