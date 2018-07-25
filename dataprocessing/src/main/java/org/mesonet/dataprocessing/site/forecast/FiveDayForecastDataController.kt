package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import io.reactivex.subjects.BehaviorSubject

interface FiveDayForecastDataController
{
    fun GetCount(): Int
    fun GetForecast(inIndex: Int): SemiDayForecastDataController
    fun GetForecastDataSubject(inContext: Context): BehaviorSubject<List<SemiDayForecastDataController>>
    fun Dispose()
}