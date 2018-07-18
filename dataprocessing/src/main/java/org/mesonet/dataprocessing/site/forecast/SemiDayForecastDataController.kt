package org.mesonet.dataprocessing.site.forecast

import android.content.Context


import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.DefaultUnits
import java.util.Locale

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.R

import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.forecast.Forecast
import org.mesonet.network.DataDownloader


class SemiDayForecastDataController(private var mContext: Context?, private var mPreferences: Preferences, private var mUnitConverter: UnitConverter?, inForecast: Forecast?, val mDataDownloader: DataDownloader){
    val mDataSubject: BehaviorSubject<ForecastData> = BehaviorSubject.create()

    init {
        Observable.create(ObservableOnSubscribe<Void>{
            SetData(inForecast)
        }).subscribeOn(Schedulers.computation()).subscribe()
    }


    fun GetForecastDataSubject(): BehaviorSubject<ForecastData>
    {
        return mDataSubject
    }

    internal fun SetData(inForecast: Forecast?) {
        mPreferences.UnitPreferencesSubject().subscribe(object: Observer<Preferences.UnitPreference>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Preferences.UnitPreference) {
                val result = ForecastDataImpl()
                if(inForecast != null)
                {
                    if (inForecast.GetTime() != null)
                        result.mTime = inForecast.GetTime()?: ""

                    if (mContext != null && mContext?.resources?.getBoolean(R.bool.forceWrapForecasts) == true && !result.mTime.contains(" "))
                        result.mTime += "\n"

                    if (inForecast.GetIconUrl() != null) {
                        val sizeModifier = mContext?.resources?.getString(R.string.forecastImageRes)
                        result.mIconUrl = mDataDownloader.GetForecastImage(inForecast.GetIconUrl()?.removePrefix("http://www.nws.noaa.gov/weather/images/fcicons/")?.removeSuffix(".jpg")?: "", sizeModifier?: "")
                    }

                    if (inForecast.GetStatus() != null)
                        result.mStatus = inForecast.GetStatus() + "\n"

                    if (inForecast.GetHighOrLow() != null)
                        result.mHighOrLowTemp = inForecast.GetHighOrLow()?.name?: ""

                    var tempUnits: DefaultUnits.TempUnits = DefaultUnits.TempUnits.kCelsius

                    if (t == Preferences.UnitPreference.kImperial)
                        tempUnits = DefaultUnits.TempUnits.kFahrenheit

                    var unit = ""

                    when (tempUnits) {
                        DefaultUnits.TempUnits.kCelsius -> unit = "C"
                        DefaultUnits.TempUnits.kFahrenheit -> unit = "F"
                    }

                    var value = mUnitConverter?.GetTempInPreferredUnits(inForecast.GetTemp(), inForecast, tempUnits)?.toInt()

                    result.mHighOrLowTemp += " " + String.format(Locale.getDefault(), "%d", value) + "°" + unit

                    if (inForecast.GetWindMin() == null)
                        result.mWindDescription = "Calm\n"
                    else {
                        var speedUnits: DefaultUnits.SpeedUnits = DefaultUnits.SpeedUnits.kMps

                        if (t == Preferences.UnitPreference.kImperial)
                            speedUnits = DefaultUnits.SpeedUnits.kMph

                        unit = ""

                        when (speedUnits) {
                            DefaultUnits.SpeedUnits.kMps -> unit = "mps"
                            DefaultUnits.SpeedUnits.kMph -> unit = "mph"
                            DefaultUnits.SpeedUnits.kKmph -> unit = "kmph"
                        }

                        if (mUnitConverter == null)
                            result.mWindDescription = ""

                        var directionDesc: String

                        if (inForecast.GetWindDirectionStart() != null) {
                            directionDesc = inForecast.GetWindDirectionStart()?.name?: ""

                            if (inForecast.GetWindDirectionStart() != inForecast.GetWindDirectionEnd())
                                directionDesc += "-" + inForecast.GetWindDirectionEnd()

                            value = mUnitConverter?.GetSpeedInPreferredUnits(inForecast.GetWindMin(), inForecast, speedUnits)?.toInt()
                            val value2 = mUnitConverter?.GetSpeedInPreferredUnits(inForecast.GetWindMax(), inForecast, speedUnits)?.toInt()

                            var finalWindSpdValue = value.toString()
                            if (value != value2)
                                finalWindSpdValue += "-$value2"

                            result.mWindDescription = "Wind $directionDesc $finalWindSpdValue $unit"
                        }
                    }

                    if(!mDataSubject.hasValue() || mDataSubject.value.compareTo(result) != 0)
                        mDataSubject.onNext(result)
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                onNext(Preferences.UnitPreference.kImperial)
            }

        })
    }

    class ForecastDataImpl(internal var mTime: String = "",
                           internal var mIconUrl: String = "",
                           internal var mStatus: String = "",
                           internal var mHighOrLowTemp: String = "",
                           internal var mWindDescription: String = ""): ForecastData {
        override fun compareTo(other: ForecastData): Int {
            var result = GetTime().compareTo(other.GetTime())

            if(result == 0)
                result = GetIconUrl().compareTo(other.GetIconUrl())

            if(result == 0)
                result = GetStatus().compareTo(other.GetStatus())

            if(result == 0)
                result = GetHighOrLowTemp().compareTo(other.GetHighOrLowTemp())

            if(result == 0)
                result = GetWindDescription().compareTo(other.GetWindDescription())

            return result
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

        override fun GetHighOrLowTemp(): String {
            return mHighOrLowTemp
        }


        override fun GetWindDescription(): String {
            return mWindDescription
        }
    }
}
