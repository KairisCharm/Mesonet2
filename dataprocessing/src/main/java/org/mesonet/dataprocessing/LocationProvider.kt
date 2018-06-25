package org.mesonet.dataprocessing

import android.location.Location
import io.reactivex.Observable

interface LocationProvider
{
    fun GetLocation(inLocationListener: LocationListener): Observable<Location>

    interface LocationListener {
        fun LastLocationFound(inLocation: Location?)
        fun LocationUnavailable()
    }
}