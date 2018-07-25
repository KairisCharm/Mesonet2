package org.mesonet.dataprocessing.site.mesonetdata

import android.content.Context
import io.reactivex.subjects.BehaviorSubject

interface MesonetUIController
{
    fun GetDisplayFieldsSubject(inContext: Context): BehaviorSubject<MesonetDisplayFields>

    interface MesonetDisplayFields
    {
        fun GetStationName(): String
        fun GetAirTempString(): String
        fun GetApparentTempString(): String
        fun GetDewpointString(): String
        fun GetWindString(): String
        fun Get24HrRainfallString(): String
        fun GetHumidityString(): String
        fun GetWindGustsString(): String
        fun GetPressureString(): String
        fun GetTimeString(): String
        fun IsFavorite(): Boolean
    }
}