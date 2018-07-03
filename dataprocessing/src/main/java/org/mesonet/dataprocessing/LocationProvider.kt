package org.mesonet.dataprocessing

import android.content.Context
import android.location.Location
import io.reactivex.Observable

interface LocationProvider
{
    fun GetLocation(inContext: Context): Observable<LocationResult>


    interface LocationResult
    {
        fun LocationResult(): Location?
    }
}