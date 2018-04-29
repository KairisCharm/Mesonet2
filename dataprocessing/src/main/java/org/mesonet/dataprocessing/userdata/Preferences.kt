package org.mesonet.dataprocessing.userdata


interface Preferences {
    enum class UnitPreference {
        kMetric, kImperial
    }


    fun GetUnitPreference(inListener: UnitPreferenceListener)
    fun SetUnitPreference(inPreference: UnitPreference)
    fun GetSelectedStid(inListener: StidListener)
    fun SetSelectedStid(inStid: String)
    fun GetSelectedRadar(inListener: RadarListener)
    fun SetSelectedRadar(inRadarName: String)
    fun GetPreferencesObservable(): PreferencesObservable

    interface UnitPreferenceListener
    {
        fun UnitPreferenceFound(inUnitPreference: UnitPreference)
    }
    interface StidListener
    {
        fun StidFound(inStidPreference: String)
    }

    interface RadarListener
    {
        fun RadarFound(inRadarName: String)
    }
}