package org.mesonet.androidsystem

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import org.mesonet.dataprocessing.LocationProvider
import android.content.Context
import io.reactivex.ObservableOnSubscribe

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceLocation @Inject
constructor() : LocationProvider {

    @Inject
    internal lateinit var mPermissions: Permissions


    override fun GetLocation(inContext: Context): Observable<LocationProvider.LocationResult> {
        return Observable.create (ObservableOnSubscribe<LocationProvider.LocationResult>{ locationSubscriber ->
            mPermissions.RequestPermission(inContext, Manifest.permission.ACCESS_FINE_LOCATION).observeOn(Schedulers.computation()).subscribe {
                if (it && ContextCompat.checkSelfPermission(inContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.getFusedLocationProviderClient(inContext).requestLocationUpdates(LocationRequest.create(), object : LocationCallback() {
                        override fun onLocationResult(inResult: com.google.android.gms.location.LocationResult?) {
                            locationSubscriber.onNext(LocationResultImpl(inResult?.lastLocation))

                            LocationServices.getFusedLocationProviderClient(inContext).removeLocationUpdates(this)
                        }
                    }, null)
                }
            }
        }).subscribeOn(Schedulers.computation())
    }


    class LocationResultImpl(val mLocation: Location?): LocationProvider.LocationResult
    {
        override fun LocationResult(): Location? {
            return mLocation
        }
    }
}