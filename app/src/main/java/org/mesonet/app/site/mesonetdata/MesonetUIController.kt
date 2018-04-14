package org.mesonet.app.site.mesonetdata

import org.mesonet.app.dependencyinjection.PerFragment
import org.mesonet.app.userdata.Preferences

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Observable
import java.util.Observer
import java.util.TimeZone

import javax.inject.Inject


//TODO TESTS!!!!!
@PerFragment
class MesonetUIController @Inject
constructor(internal var mDataController: MesonetDataController?) : Observable(), Observer {



    init {
        mDataController!!.addObserver(this)
    }


    override fun update(observable: Observable, o: Any?) {
        setChanged()
        notifyObservers()
    }


    fun GetAirTempString(): String {
        if (mDataController == null)
            return "-"

        val temp = mDataController!!.GetTemp() ?: return "-"

        return String.format(Locale.getDefault(), "%.0f", temp) + "°"
    }


    fun GetApparentTempString(): String {
        if (mDataController == null)
            return "-"

        val apparentTemp = mDataController!!.GetApparentTemp() ?: return "-"

        var unit = ""

        val unitPreference = mDataController!!.GetUnitPreference()

        if (unitPreference != null) {
            when (unitPreference) {
                Preferences.UnitPreference.kMetric -> unit = "C"
                Preferences.UnitPreference.kImperial -> unit = "F"
            }
        }

        return String.format(Locale.getDefault(), "%.0f", apparentTemp) + "°" + unit
    }


    fun GetDewpointString(): String {
        if (mDataController == null)
            return "-"

        val dewPoint = mDataController!!.GetDewpoint() ?: return "-"

        var unit = ""

        when (mDataController!!.GetUnitPreference()) {
            Preferences.UnitPreference.kMetric -> unit = "C"
            Preferences.UnitPreference.kImperial -> unit = "F"
        }

        return String.format(Locale.getDefault(), "%.0f", dewPoint) + "°" + unit
    }


    fun GetWindString(): String {
        if (mDataController == null)
            return "-"

        val windSpd = mDataController!!.GetWindSpd()
        val direction = mDataController!!.GetWindDirection()

        if (windSpd == null || direction == null)
            return "-"

        var unit = ""

        when (mDataController!!.GetUnitPreference()) {
            Preferences.UnitPreference.kMetric -> unit = "mps"
            Preferences.UnitPreference.kImperial -> unit = "mph"
        }

        return direction.name + " at " + String.format(Locale.getDefault(), "%.0f", windSpd) + " " + unit
    }


    fun Get24HrRainfallString(): String {
        if (mDataController == null)
            return "-"

        val rain24h = mDataController!!.Get24HrRain() ?: return "-"

        var unit = ""

        when (mDataController!!.GetUnitPreference()) {
            Preferences.UnitPreference.kMetric -> unit = "mm"
            Preferences.UnitPreference.kImperial -> unit = "in"
        }

        return String.format(Locale.getDefault(), "%.2f", rain24h) + " " + unit
    }


    fun GetHumidityString(): String {
        if (mDataController == null)
            return "-"

        val humidity = mDataController!!.GetHumidity() ?: return "-"

        return humidity.toString() + "%"
    }


    fun GetWindGustsString(): String {
        if (mDataController == null)
            return "-"

        val windSpd = mDataController!!.GetMaxWind() ?: return "-"

        var unit = ""

        when (mDataController!!.GetUnitPreference()) {
            Preferences.UnitPreference.kMetric -> unit = "mps"
            Preferences.UnitPreference.kImperial -> unit = "mph"
        }

        return String.format(Locale.getDefault(), "%.0f", windSpd) + " " + unit
    }


    fun GetPressureString(): String {
        if (mDataController == null)
            return "-"

        val pressure = mDataController!!.GetPressure() ?: return "-"

        var unit = ""
        var format = ""

        when (mDataController!!.GetUnitPreference()) {
            Preferences.UnitPreference.kMetric -> {
                unit = "mb"
                format = "%.1f"
            }
            Preferences.UnitPreference.kImperial -> {
                unit = "inHg"
                format = "%.2f"
            }
        }

        return String.format(Locale.getDefault(), format, pressure) + " " + unit
    }


    fun GetTimeString(): String {
        val formattedString = "Observed at %s"

        if (mDataController == null)
            return String.format(formattedString, "-:-")

        val time = mDataController!!.GetTime() ?: return String.format(formattedString, "-:-")

        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("America/Chicago")

        return String.format(formattedString, dateFormat.format(time))
    }
}
