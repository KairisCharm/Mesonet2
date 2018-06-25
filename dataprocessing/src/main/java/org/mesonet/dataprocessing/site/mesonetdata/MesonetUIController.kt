package org.mesonet.dataprocessing.site.mesonetdata

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import org.mesonet.dataprocessing.userdata.Preferences

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class MesonetUIController @Inject
constructor(private var mDataController: MesonetDataController, private var mPreferences: Preferences) : Observable<MesonetUIController.MesonetDisplayFields>()
{
    override fun subscribeActual(observer: Observer<in MesonetDisplayFields>?) {
        if(observer != null) {
            mPreferences.GetUnitPreferencesObservable().observeOn(Schedulers.computation()).subscribe { unitPreference ->
                mDataController.observeOn(Schedulers.computation()).map {
                    val displayFields = MesonetDisplayFieldsImpl()
                    val formattedString = "Observed at %s"

                    val time = mDataController.ProcessTime()

                    if (time == null)
                        displayFields.mTimeString = String.format(formattedString, "-:-")
                    else {
                        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                        dateFormat.timeZone = TimeZone.getTimeZone("America/Chicago")

                        displayFields.mTimeString = String.format(formattedString, dateFormat.format(time))
                    }

                    val temp = mDataController.ProcessTemp(unitPreference)

                    if (temp == null)
                        displayFields.mAirTempString = "-"
                    else
                        displayFields.mAirTempString = String.format(Locale.getDefault(), "%.0f", temp) + "°"

                    val apparentTemp = mDataController.ProcessApparentTemp(unitPreference)

                    if (apparentTemp == null)
                        displayFields.mApparentTempString = "-"
                    else {
                        var unit = ""

                        when (unitPreference) {
                            Preferences.UnitPreference.kMetric -> unit = "C"
                            Preferences.UnitPreference.kImperial -> unit = "F"
                        }

                        displayFields.mApparentTempString = String.format(Locale.getDefault(), "%.0f", apparentTemp) + "°" + unit
                    }

                    val dewPoint = mDataController.ProcessDewpoint(unitPreference)

                    if (dewPoint == null)
                        displayFields.mDewpointString = "-"
                    else {
                        var unit = ""

                        when (unitPreference) {
                            Preferences.UnitPreference.kMetric -> unit = "C"
                            Preferences.UnitPreference.kImperial -> unit = "F"
                        }

                        displayFields.mDewpointString = String.format(Locale.getDefault(), "%.0f", dewPoint) + "°" + unit
                    }

                    val windSpd = mDataController.ProcessWindSpd(unitPreference)
                    val direction = mDataController.ProcessWindDirection()

                    if (windSpd == null || direction == null)
                        displayFields.mWindString = "-"
                    else {
                        var unit = ""

                        when (unitPreference) {
                            Preferences.UnitPreference.kMetric -> unit = "mps"
                            Preferences.UnitPreference.kImperial -> unit = "mph"
                        }

                        displayFields.mWindString = direction.name + " at " + String.format(Locale.getDefault(), "%.0f", windSpd) + " " + unit
                    }

                    val rain24h = mDataController.Process24HrRain(unitPreference)

                    if (rain24h == null)
                        displayFields.m24HrRainfallString = "-"
                    else {
                        var unit = ""

                        when (unitPreference) {
                            Preferences.UnitPreference.kMetric -> unit = "mm"
                            Preferences.UnitPreference.kImperial -> unit = "in"
                        }

                        displayFields.m24HrRainfallString = String.format(Locale.getDefault(), "%.2f", rain24h) + " " + unit
                    }

                    val humidity = mDataController.ProcessHumidity()

                    if (humidity == null)
                        displayFields.mHumidityString = "-"
                    else
                        displayFields.mHumidityString = humidity.toString() + "%"

                    val windGusts = mDataController.ProcessMaxWind(unitPreference)

                    if (windGusts == null)
                        displayFields.mWindGustsString = "-"
                    else {
                        var unit = ""

                        when (unitPreference) {
                            Preferences.UnitPreference.kMetric -> unit = "mps"
                            Preferences.UnitPreference.kImperial -> unit = "mph"
                        }

                        displayFields.mWindGustsString = String.format(Locale.getDefault(), "%.0f", windGusts) + " " + unit
                    }

                    val pressure = mDataController.ProcessPressure(unitPreference)

                    if (pressure == null)
                        displayFields.mPressureString = "-"
                    else {
                        var unit = ""
                        var format = ""

                        when (unitPreference) {
                            Preferences.UnitPreference.kMetric -> {
                                unit = "mb"
                                format = "%.1f"
                            }
                            Preferences.UnitPreference.kImperial -> {
                                unit = "inHg"
                                format = "%.2f"
                            }
                        }

                        displayFields.mPressureString = String.format(Locale.getDefault(), format, pressure) + " " + unit
                    }

                    displayFields as MesonetDisplayFields
                }.subscribe(observer)
            }
        }
    }



    private class MesonetDisplayFieldsImpl(internal var mTimeString: String = "Observed at -:-",
                                         internal var mAirTempString: String = "-",
                                         internal var mApparentTempString: String = "-",
                                         internal var mDewpointString: String = "-",
                                         internal var mWindString: String = "-",
                                         internal var m24HrRainfallString: String = "-",
                                         internal var mHumidityString: String = "-",
                                         internal var mWindGustsString: String = "-",
                                         internal var mPressureString: String = "-") : MesonetDisplayFields {

        override fun GetAirTempString(): String {
            return mAirTempString
        }


        override fun GetApparentTempString(): String {
            return mApparentTempString
        }


        override fun GetDewpointString(): String {
            return mDewpointString
        }


        override fun GetWindString(): String {
            return mWindString
        }


        override fun Get24HrRainfallString(): String {
            return m24HrRainfallString
        }


        override fun GetHumidityString(): String {
            return mHumidityString
        }


        override fun GetWindGustsString(): String {
            return mWindGustsString
        }


        override fun GetPressureString(): String {
            return mPressureString
        }


        override fun GetTimeString(): String {
            return mTimeString
        }
    }


    interface MesonetDisplayFields
    {
        fun GetAirTempString(): String
        fun GetApparentTempString(): String
        fun GetDewpointString(): String
        fun GetWindString(): String
        fun Get24HrRainfallString(): String
        fun GetHumidityString(): String
        fun GetWindGustsString(): String
        fun GetPressureString(): String
        fun GetTimeString(): String
    }
}