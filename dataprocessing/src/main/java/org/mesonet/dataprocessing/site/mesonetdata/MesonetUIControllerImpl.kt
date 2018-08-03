package org.mesonet.dataprocessing.site.mesonetdata

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerContext
import org.mesonet.dataprocessing.userdata.Preferences

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

import javax.inject.Inject


@PerContext
class MesonetUIControllerImpl @Inject
constructor(private var mDataController: MesonetDataController,
            private var mPreferences: Preferences): MesonetUIController
{
    private var mUISubject: BehaviorSubject<MesonetUIController.MesonetDisplayFields> = BehaviorSubject.create()

    private var mUnitPreferenceDisposable: Disposable? = null



    override fun GetDisplayFieldsSubject(inContext: Context): BehaviorSubject<MesonetUIController.MesonetDisplayFields>
    {
        if(!mUISubject.hasValue())
        {
            var valueDisposable: Disposable? = null
            mPreferences.UnitPreferencesSubject(inContext).observeOn(Schedulers.computation()).subscribe(object: Observer<Preferences.UnitPreference>{
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {
                    mUnitPreferenceDisposable = d
                }

                override fun onNext(unitPreference: Preferences.UnitPreference) {
                    valueDisposable?.dispose()
                    mDataController.GetDataSubject(inContext).observeOn(Schedulers.computation()).map {
                        val displayFields = MesonetDisplayFieldsImpl()
                        val formattedString = "Observed at %s"

                        displayFields.mStationName = mDataController.GetStationName()
                        displayFields.mIsFavorite = mDataController.StationIsFavorite()

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
                            displayFields.mAirTempString = String.format(Locale.getDefault(), if(unitPreference == Preferences.UnitPreference.kImperial) "%.0f" else "%.1f", temp) + "°"

                        val apparentTemp = mDataController.ProcessApparentTemp(unitPreference)

                        if (apparentTemp == null)
                            displayFields.mApparentTempString = "-"
                        else {
                            var unit = ""
                            var format = ""

                            when (unitPreference) {
                                Preferences.UnitPreference.kMetric ->
                                {
                                    unit = "C"
                                    format = "%.1f"
                                }
                                Preferences.UnitPreference.kImperial ->
                                {
                                    unit = "F"
                                    format = "%.0f"
                                }
                            }

                            displayFields.mApparentTempString = String.format(Locale.getDefault(), format, apparentTemp) + "°" + unit
                        }

                        val dewPoint = mDataController.ProcessDewpoint(unitPreference)

                        if (dewPoint == null)
                            displayFields.mDewPointString = "-"
                        else {
                            var unit = ""
                            var format = ""

                            when (unitPreference) {
                                Preferences.UnitPreference.kMetric ->
                                {
                                    unit = "C"
                                    format = "%.1f"
                                }
                                Preferences.UnitPreference.kImperial ->
                                {
                                    unit = "F"
                                    format = "%.0f"
                                }
                            }

                            displayFields.mDewPointString = String.format(Locale.getDefault(), format, dewPoint) + "°" + unit
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

                        displayFields as MesonetUIController.MesonetDisplayFields
                    }.subscribe(object : Observer<MesonetUIController.MesonetDisplayFields> {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable)
                        {
                            valueDisposable = d
                        }
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                        override fun onNext(t: MesonetUIController.MesonetDisplayFields) {
                            mUISubject.onNext(t)
                        }
                    })
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext(Preferences.UnitPreference.kImperial)
                }

            })
        }
        return mUISubject
    }


    fun Dispose()
    {
        mUnitPreferenceDisposable?.dispose()
        mDataController.Dispose()
        mUISubject.onComplete()
    }



    private class MesonetDisplayFieldsImpl(internal var mStationName: String = "",
                                           internal var mTimeString: String = "Observed at -:-",
                                           internal var mAirTempString: String = "-",
                                           internal var mApparentTempString: String = "-",
                                           internal var mDewPointString: String = "-",
                                           internal var mWindString: String = "-",
                                           internal var m24HrRainfallString: String = "-",
                                           internal var mHumidityString: String = "-",
                                           internal var mWindGustsString: String = "-",
                                           internal var mPressureString: String = "-",
                                           internal var mIsFavorite: Boolean = false) : MesonetUIController.MesonetDisplayFields {
        override fun GetStationName(): String {
            return mStationName
        }

        override fun GetAirTempString(): String {
            return mAirTempString
        }


        override fun GetApparentTempString(): String {
            return mApparentTempString
        }


        override fun GetDewpointString(): String {
            return mDewPointString
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

        override fun IsFavorite(): Boolean {
            return mIsFavorite
        }
    }
}