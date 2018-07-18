package org.mesonet.app.widget

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.mesonet.dataprocessing.LocationProvider
import javax.inject.Inject



class EmptyLocationProvider @Inject constructor() : LocationProvider {
    override fun GetLocation(inContext: Context?): Observable<LocationProvider.LocationResult> {
        return Observable.create {  }
    }
}