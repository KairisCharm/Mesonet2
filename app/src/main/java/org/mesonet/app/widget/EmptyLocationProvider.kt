package org.mesonet.app.widget

import android.content.Context
import android.location.Location
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.mesonet.dataprocessing.LocationProvider
import javax.inject.Inject



class EmptyLocationProvider @Inject constructor() : LocationProvider {
    override fun RegisterGpsResult(inResultCode: Int) {
    }

    override fun GetLocation(): Observable<LocationProvider.LocationResult> {
        return Observable.create {
            it.onNext(object: LocationProvider.LocationResult{
                override fun LocationResult(): Location? {
                    return null
                }

            })

        }
    }
}