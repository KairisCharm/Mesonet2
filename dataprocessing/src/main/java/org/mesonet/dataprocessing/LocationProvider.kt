package org.mesonet.dataprocessing

import android.content.Context
import android.location.Location
import io.reactivex.Observable

interface LocationProvider
{
    fun GetLocation(): Observable<LocationResult>
    fun RegisterGpsResult(inResultCode: Int)


    interface LocationResult
    {
        fun LocationResult(): Location?
    }
}