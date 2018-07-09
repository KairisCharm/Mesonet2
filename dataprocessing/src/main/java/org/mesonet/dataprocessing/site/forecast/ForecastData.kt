package org.mesonet.dataprocessing.site.forecast


interface ForecastData: Comparable<ForecastData> {
    fun GetTime(): String
    fun GetIconUrl(): String
    fun GetStatus(): String
    fun GetHighOrLow(): String
    fun GetTemp(): String
    fun GetWindDescription(): String
}