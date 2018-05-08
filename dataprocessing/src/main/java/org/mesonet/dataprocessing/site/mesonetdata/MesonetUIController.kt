package org.mesonet.dataprocessing.site.mesonetdata

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.userdata.Preferences

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Observable
import java.util.Observer
import java.util.TimeZone

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MesonetUIController @Inject
constructor(private var mDataController: MesonetDataController, private var mThreadHandler: ThreadHandler, private var mPreferences: Preferences) : Observable(), Observer {
    private var mTimeString: String = "Observed at -:-"
    private var mAirTempString: String = "-"
    private var mApparentTempString: String = "-"
    private var mDewpointString: String = "-"
    private var mWindString: String = "-"
    private var m24HrRainfallString: String = "-"
    private var mHumidityString: String = "-"
    private var mWindGustsString: String = "-"
    private var mPressureString: String = "-"

    private var mDoingInitialUpdate = true

    init {
        mThreadHandler.Run("MesonetData", Runnable {
            mDataController.addObserver(this)
            Update2()
        })
    }


    override fun update(observable: Observable, o: Any?) {
        mThreadHandler.Run("MesonetData", Runnable {
            Update2()
        })
    }


    internal fun Update2() {
        mPreferences.GetUnitPreference(object : Preferences.UnitPreferenceListener {
            override fun UnitPreferenceFound(inUnitPreference: Preferences.UnitPreference) {
                val formattedString = "Observed at %s"

                val time = mDataController.ProcessTime()

                if (time == null)
                    mTimeString = String.format(formattedString, "-:-")
                else {
                    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("America/Chicago")

                    mTimeString = String.format(formattedString, dateFormat.format(time))
                }

                val temp = mDataController.ProcessTemp(inUnitPreference)

                if (temp == null)
                    mAirTempString = "-"
                else
                    mAirTempString = String.format(Locale.getDefault(), "%.0f", temp) + "°"

                val apparentTemp = mDataController.ProcessApparentTemp(inUnitPreference)

                if (apparentTemp == null)
                    mApparentTempString = "-"
                else {
                    var unit = ""

                    when (inUnitPreference) {
                        Preferences.UnitPreference.kMetric -> unit = "C"
                        Preferences.UnitPreference.kImperial -> unit = "F"
                    }

                    mApparentTempString = String.format(Locale.getDefault(), "%.0f", apparentTemp) + "°" + unit
                }

                val dewPoint = mDataController.ProcessDewpoint(inUnitPreference)

                if (dewPoint == null)
                    mDewpointString = "-"
                else {
                    var unit = ""

                    when (inUnitPreference) {
                        Preferences.UnitPreference.kMetric -> unit = "C"
                        Preferences.UnitPreference.kImperial -> unit = "F"
                    }

                    mDewpointString = String.format(Locale.getDefault(), "%.0f", dewPoint) + "°" + unit
                }

                val windSpd = mDataController.ProcessWindSpd(inUnitPreference)
                val direction = mDataController.ProcessWindDirection()

                if (windSpd == null || direction == null)
                    mWindString = "-"
                else {
                    var unit = ""

                    when (inUnitPreference) {
                        Preferences.UnitPreference.kMetric -> unit = "mps"
                        Preferences.UnitPreference.kImperial -> unit = "mph"
                    }

                    mWindString = direction.name + " at " + String.format(Locale.getDefault(), "%.0f", windSpd) + " " + unit
                }

                val rain24h = mDataController.Process24HrRain(inUnitPreference)

                if (rain24h == null)
                    m24HrRainfallString = "-"
                else {
                    var unit = ""

                    when (inUnitPreference) {
                        Preferences.UnitPreference.kMetric -> unit = "mm"
                        Preferences.UnitPreference.kImperial -> unit = "in"
                    }

                    m24HrRainfallString = String.format(Locale.getDefault(), "%.2f", rain24h) + " " + unit
                }

                val humidity = mDataController.ProcessHumidity()

                if (humidity == null)
                    mHumidityString = "-"
                else
                    mHumidityString = humidity.toString() + "%"

                val windGusts = mDataController.ProcessMaxWind(inUnitPreference)

                if (windGusts == null)
                    mWindGustsString = "-"
                else {
                    var unit = ""

                    when (inUnitPreference) {
                        Preferences.UnitPreference.kMetric -> unit = "mps"
                        Preferences.UnitPreference.kImperial -> unit = "mph"
                    }

                    mWindGustsString = String.format(Locale.getDefault(), "%.0f", windGusts) + " " + unit
                }

                val pressure = mDataController.ProcessPressure(inUnitPreference)

                if (pressure == null)
                    mPressureString = "-"
                else {
                    var unit = ""
                    var format = ""

                    when (inUnitPreference) {
                        Preferences.UnitPreference.kMetric -> {
                            unit = "mb"
                            format = "%.1f"
                        }
                        Preferences.UnitPreference.kImperial -> {
                            unit = "inHg"
                            format = "%.2f"
                        }
                    }

                    mPressureString = String.format(Locale.getDefault(), format, pressure) + " " + unit
                }


                mDoingInitialUpdate = time == null && temp == null && apparentTemp == null &&
                        dewPoint == null && windSpd == null && direction == null &&
                        rain24h == null && humidity == null && windGusts == null
                setChanged()
                notifyObservers()
            }
        })
    }

    @Override
    override fun addObserver(inObserver: Observer) {
        mThreadHandler.Run("MesonetData", Runnable {
            super.addObserver(inObserver)
            setChanged()
            notifyObservers()
        })
    }


    fun SingleUpdate(inSingleUpdateListener: MesonetDataController.SingleUpdateListener) {
        mDataController.SingleUpdate(object : MesonetDataController.SingleUpdateListener {
            override fun UpdateComplete() {
                Update2()
                inSingleUpdateListener.UpdateComplete()
            }

            override fun UpdateFailed() {
                inSingleUpdateListener.UpdateFailed()
            }

        })
    }



    fun DoingInitialUpdate(): Boolean
    {
        return mDoingInitialUpdate
    }



    fun GetAirTempString(): String {
        return mAirTempString
    }


    fun GetApparentTempString(): String {
        return mApparentTempString
    }


    fun GetDewpointString(): String {
        return mDewpointString
    }


    fun GetWindString(): String {
        return mWindString
    }


    fun Get24HrRainfallString(): String {
        return m24HrRainfallString
    }


    fun GetHumidityString(): String {
        return mHumidityString
    }


    fun GetWindGustsString(): String {
        return mWindGustsString
    }


    fun GetPressureString(): String {
        return mPressureString
    }


    fun GetTimeString(): String
    {
        return mTimeString
    }
}
