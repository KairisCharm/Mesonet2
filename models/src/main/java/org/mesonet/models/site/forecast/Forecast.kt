package org.mesonet.models.site.forecast

import org.mesonet.core.DefaultUnits


interface Forecast : DefaultUnits
{
    fun GetTime(): String?
    fun GetTemp(): Number?
    fun GetIconId(): String?
    fun GetStatus(): String?
    fun GetHighOrLow(): HighOrLow?
    fun GetWindDirectionStart(): DefaultUnits.CompassDirections?
    fun GetWindDirectionEnd(): DefaultUnits.CompassDirections?
    fun GetWindMin(): Number?
    fun GetWindMax(): Number?


    enum class HighOrLow {
        High, Low
    }
}