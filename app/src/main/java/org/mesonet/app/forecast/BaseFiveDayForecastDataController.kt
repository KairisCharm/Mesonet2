package org.mesonet.app.forecast


import android.app.Activity
import android.content.Context

import org.mesonet.app.formulas.UnitConverter
import org.mesonet.app.reflection.ForecastModelParser
import org.mesonet.app.userdata.Preferences

import java.util.ArrayList
import java.util.Observable

import javax.inject.Inject

open class BaseFiveDayForecastDataController @Inject constructor() : Observable() {
    @Inject
    lateinit var mModelParser: ForecastModelParser

    @Inject
    lateinit var mPreferences: Preferences

    @Inject
    lateinit var mUnitConverter: UnitConverter

    @Inject
    lateinit var mActivity: Activity

    internal var mSemiDayForecasts: MutableList<SemiDayForecastDataController> = ArrayList()


    fun SetData(inForecast: String?) {
        SetData(mModelParser.Parse(inForecast))
    }


    fun SetData(inForecast: List<ForecastModel>?) {
        mSemiDayForecasts = ArrayList()

        if (inForecast != null) {

            for (i in inForecast.indices) {
                mSemiDayForecasts.add(SemiDayForecastDataController(mActivity, mPreferences, mUnitConverter, inForecast[i]))
            }
        }

        setChanged()
        notifyObservers()
    }


    fun GetCount(): Int {
        return mSemiDayForecasts.size
    }


    fun GetForecast(inIndex: Int): SemiDayForecastDataController {
        return mSemiDayForecasts[inIndex]
    }


    fun GetObservable(): Observable {
        return this
    }
}
