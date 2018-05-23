package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import org.mesonet.core.DefaultUnits
import org.mesonet.core.ThreadHandler

import java.util.Locale
import java.util.Observable
import org.mesonet.dataprocessing.formulas.UnitConverter

import org.mesonet.dataprocessing.R
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.forecast.Forecast


class SemiDayForecastDataController(private var mContext: Context?, private var mPreferences: Preferences, private var mUnitConverter: UnitConverter?, private var mForecast: Forecast, private var mThreadHandler: ThreadHandler) : Observable(), ForecastData {

    private var mTime: String = ""
    private var mIconUrl: String = ""
    private var mStatus: String = ""
    private var mHighOrLow: String = ""
    private var mTemp: String = ""
    private var mWindDescription: String = ""

    init {
        mThreadHandler.Run("ForecastData", Runnable {
            SetData(mForecast)
        })
    }

    internal fun SetData(inForecast: Forecast) {
        mForecast = inForecast
        mPreferences.GetUnitPreference(object: Preferences.UnitPreferenceListener{
            override fun UnitPreferenceFound(inUnitPreference: Preferences.UnitPreference) {
                if(mForecast.GetTime() != null)
                    mTime = mForecast.GetTime()!!

                if (mContext != null && mContext!!.resources.getBoolean(R.bool.forceWrapForecasts) && !mTime.contains(" "))
                    mTime += "\n"

                if(mForecast.GetIconUrl() != null)
                    mIconUrl = mForecast.GetIconUrl()!!.replace("http://www.nws.noaa.gov/weather/images/fcicons", "http://www.mesonet.org/images/fcicons-android").replace(".jpg", "@4x.png")

                if(mForecast.GetStatus() != null)
                    mStatus = mForecast.GetStatus() + "\n"

                if(mForecast.GetHighOrLow() != null)
                    mHighOrLow = mForecast.GetHighOrLow()!!.name

                var tempUnits: DefaultUnits.TempUnits = DefaultUnits.TempUnits.kCelsius

                if (inUnitPreference == Preferences.UnitPreference.kImperial)
                    tempUnits = DefaultUnits.TempUnits.kFahrenheit

                var unit = ""

                when (tempUnits) {
                    DefaultUnits.TempUnits.kCelsius -> unit = "C"
                    DefaultUnits.TempUnits.kFahrenheit -> unit = "F"
                }

                if (mUnitConverter == null)
                    mTemp = ""

                var value = mUnitConverter!!.GetTempInPreferredUnits(mForecast.GetTemp(), mForecast, tempUnits)!!.toInt()

                mTemp = String.format(Locale.getDefault(), "%d", value) + "°" + unit

                if (mForecast.GetWindMin() == null)
                    mWindDescription = "Calm\n"

                else {
                    var speedUnits: DefaultUnits.SpeedUnits = DefaultUnits.SpeedUnits.kMps

                    if (inUnitPreference == Preferences.UnitPreference.kImperial)
                        speedUnits = DefaultUnits.SpeedUnits.kMph

                    unit = ""

                    when (speedUnits) {
                        DefaultUnits.SpeedUnits.kMps -> unit = "mps"
                        DefaultUnits.SpeedUnits.kMph -> unit = "mph"
                    }

                    if (mUnitConverter == null)
                        mWindDescription = ""

                    var directionDesc = mForecast.GetWindDirectionStart()!!.name

                    if(!(mForecast.GetWindDirectionStart()!!.equals(mForecast.GetWindDirectionEnd())))
                        directionDesc += "-" + mForecast.GetWindDirectionEnd()

                    value = mUnitConverter!!.GetSpeedInPreferredUnits(mForecast.GetWindMin(), mForecast, speedUnits)!!.toInt()
                    val value2 = mUnitConverter!!.GetSpeedInPreferredUnits(mForecast.GetWindMax(), mForecast, speedUnits)!!.toInt()

                    var finalWindSpdValue = value.toString()
                    if(!value.equals(value2))
                        finalWindSpdValue += "-" + value2

                    mWindDescription = "Wind " + directionDesc + " " + finalWindSpdValue + " " + unit

                }

                setChanged()
                notifyObservers()
            }

        })
    }

    override fun GetTime(): String {
        return mTime
    }

    override fun GetIconUrl(): String {
        return mIconUrl
    }

    override fun GetStatus(): String {
        return mStatus
    }

    override fun GetHighOrLow(): String {
        return mHighOrLow
    }

    override fun GetTemp(): String {
        return mTemp
    }


    override fun GetWindDescription(): String {
        return mWindDescription
    }

    override fun GetObservable(): Observable {
        return this
    }
}
