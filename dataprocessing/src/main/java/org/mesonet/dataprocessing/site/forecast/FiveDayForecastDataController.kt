package org.mesonet.dataprocessing.site.forecast

import io.reactivex.subjects.BehaviorSubject

interface FiveDayForecastDataController
{
    fun GetCount(): Int
    fun GetForecast(inIndex: Int): SemiDayForecastDataController
    fun GetForecastDataSubject(): BehaviorSubject<List<SemiDayForecastDataController>>
    fun Dispose()
}