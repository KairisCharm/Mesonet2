package org.mesonet.dataprocessing

import android.location.Location

interface LocationProvider
{
    fun GetLocation(inLocationListener: LocationListener)

    interface LocationListener {
        fun LastLocationFound(inLocation: Location?)
        fun LocationUnavailable()
    }
}