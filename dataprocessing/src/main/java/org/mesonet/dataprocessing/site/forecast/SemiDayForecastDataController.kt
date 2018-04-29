package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import org.mesonet.core.ThreadHandler

import java.util.Locale
import java.util.Observable
import org.mesonet.dataprocessing.formulas.UnitConverter

import org.mesonet.dataprocessing.R
import org.mesonet.dataprocessing.userdata.Preferences


class SemiDayForecastDataController(private var mContext: Context?, private var mPreferences: Preferences, private var mUnitConverter: UnitConverter?, private var mForecastModel: ForecastModel, private var mThreadHandler: ThreadHandler) : Observable(), ForecastData {

    private var mTime: String = ""
    private var mIconUrl: String = ""
    private var mStatus: String = ""
    private var mHighOrLow: String = ""
    private var mTemp: String = ""
    private var mWindDescription: String = ""

    init {
        mThreadHandler.Run("ForecastData", Runnable {
            SetData(mForecastModel)
        })
    }

    internal fun SetData(inForecast: ForecastModel) {
        mForecastModel = inForecast
        mPreferences.GetUnitPreference(object: Preferences.UnitPreferenceListener{
            override fun UnitPreferenceFound(inUnitPreference: Preferences.UnitPreference) {
                if(mForecastModel.mTime != null)
                    mTime = mForecastModel.mTime!!

                if (mContext != null && mContext!!.resources.getBoolean(R.bool.forceWrapForecasts) && !mTime.contains(" "))
                    mTime += "\n"

                mIconUrl = mForecastModel.mIconUrl!!.replace("http://www.nws.noaa.gov/weather/images/fcicons", "http://www.mesonet.org/images/fcicons-android").replace(".jpg", "@4x.png")

                mStatus = mForecastModel.mStatus!! + "\n"
                mHighOrLow = mForecastModel.mHighOrLow!!.name

                var tempUnits: UnitConverter.TempUnits = UnitConverter.TempUnits.kCelsius

                if (inUnitPreference == Preferences.UnitPreference.kImperial)
                    tempUnits = UnitConverter.TempUnits.kFahrenheit

                var unit = ""

                when (tempUnits) {
                    UnitConverter.TempUnits.kCelsius -> unit = "C"
                    UnitConverter.TempUnits.kFahrenheit -> unit = "F"
                }

                if (mUnitConverter == null)
                    mTemp = ""

                var value = mUnitConverter!!.GetTempInPreferredUnits(mForecastModel.mTemp, mForecastModel, tempUnits)!!.toInt()

                mTemp = String.format(Locale.getDefault(), "%d", value) + "°" + unit

                if (mForecastModel.mWindSpd == null)
                    mWindDescription = "Calm\n"

                else {
                    var speedUnits: UnitConverter.SpeedUnits = UnitConverter.SpeedUnits.kMps

                    if (inUnitPreference == Preferences.UnitPreference.kImperial)
                        speedUnits = UnitConverter.SpeedUnits.kMph

                    unit = ""

                    when (speedUnits) {
                        UnitConverter.SpeedUnits.kMps -> unit = "mps"
                        UnitConverter.SpeedUnits.kMph -> unit = "mph"
                    }

                    if (mUnitConverter == null)
                        mWindDescription = ""

                    value = mUnitConverter!!.GetSpeedInPreferredUnits(mForecastModel.mWindSpd, mForecastModel, speedUnits)!!.toInt()

                    mWindDescription = "Wind " + value.toString() + " at " + unit
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
