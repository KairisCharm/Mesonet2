package org.mesonet.app.forecast

import android.content.Context
import android.content.res.Configuration

import org.mesonet.app.R
import org.mesonet.app.dependencyinjection.PerFragment
import org.mesonet.app.formulas.DefaultUnits
import org.mesonet.app.formulas.UnitConverter
import org.mesonet.app.userdata.Preferences

import java.util.Locale
import java.util.Observable

import javax.inject.Inject

import org.mesonet.app.userdata.Preferences.UnitPreference.kImperial
import org.mesonet.app.userdata.Preferences.UnitPreference.kMetric


class SemiDayForecastDataController(internal var mContext: Context?, internal var mPreferences: Preferences?, internal var mUnitConverter: UnitConverter?, internal var mForecastModel: ForecastModel) : Observable(), ForecastData {

    fun SetData(inForecast: ForecastModel) {
        mForecastModel = inForecast

        setChanged()
        notifyObservers()
    }

    override fun GetTime(): String {
        var result = mForecastModel.mTime

        if (mContext != null && mContext!!.resources.getBoolean(R.bool.forceWrapForecasts) && !result!!.contains(" "))
            result += "\n"

        return result!!
    }

    override fun GetIconUrl(): String {
        return mForecastModel.mIconUrl!!.replace("http://www.nws.noaa.gov/weather/images/fcicons", "http://www.mesonet.org/images/fcicons-android").replace(".jpg", "@4x.png")
    }

    override fun GetStatus(): String {
        return mForecastModel.mStatus!! + "\n"
    }

    override fun GetHighOrLow(): String {
        return mForecastModel.mHighOrLow!!.name
    }

    override fun GetTemp(): String {
        var tempUnits: UnitConverter.TempUnits = UnitConverter.TempUnits.kCelsius

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        var unit = ""

        when (tempUnits) {
            UnitConverter.TempUnits.kCelsius -> unit = "C"
            UnitConverter.TempUnits.kFahrenheit -> unit = "F"
        }

        if (mUnitConverter == null)
            return ""

        val value = mUnitConverter!!.GetTempInPreferredUnits(mForecastModel.mTemp, mForecastModel, tempUnits)!!.toInt()

        return String.format(Locale.getDefault(), "%d", value) + "°" + unit
    }


    override fun GetWindDescription(): String {
        if (mForecastModel.mWindSpd == null)
            return "Calm\n"

        var speedUnits: UnitConverter.SpeedUnits = UnitConverter.SpeedUnits.kMps

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph

        var unit = ""

        when (speedUnits) {
            UnitConverter.SpeedUnits.kMps -> unit = "mps"
            UnitConverter.SpeedUnits.kMph -> unit = "mph"
        }

        if (mUnitConverter == null)
            return ""

        val value = mUnitConverter!!.GetSpeedInPreferredUnits(mForecastModel.mWindSpd, mForecastModel, speedUnits)!!.toInt()

        return "Wind " + value.toString() + " at " + unit
    }

    override fun GetObservable(): Observable {
        return this
    }
}
