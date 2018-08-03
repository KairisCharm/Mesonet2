package org.mesonet.androidsystem

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

import org.mesonet.dataprocessing.LocationProvider
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.mesonet.core.PerContext

import javax.inject.Inject


@PerContext
class DeviceLocation @Inject
constructor(var mContext: Context) : LocationProvider
{
    @Inject
    internal lateinit var mPermissions: Permissions

    val mLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)

    val mWaitingObservers: ArrayList<ObservableEmitter<LocationProvider.LocationResult>> = ArrayList()

    override fun GetLocation(): Observable<LocationProvider.LocationResult> {
        return Observable.create (ObservableOnSubscribe<LocationProvider.LocationResult>{ locationSubscriber ->
            mPermissions.RequestPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION).observeOn(Schedulers.computation()).subscribe(object: Observer<Boolean>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Boolean) {
                    if (t && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (Looper.myLooper() == null)
                            Looper.prepare()
                        val locationRequest = LocationRequest().apply {
                            interval = 1000
                            fastestInterval = 1000
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        }

                        val builder = LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)

                        val client: SettingsClient = LocationServices.getSettingsClient(mContext)
                        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

                        task.addOnSuccessListener { locationSettingsResponse ->
                            mLocationProviderClient.requestLocationUpdates(locationRequest,
                                    object : LocationCallback() {
                                        override


                                        fun onLocationResult(locationResult: LocationResult?) {
                                            if (locationResult == null) {
                                                locationSubscriber.onNext(LocationResultImpl(null))
                                                locationSubscriber.onComplete()
                                                return
                                            }

                                            locationSubscriber.onNext(LocationResultImpl(locationResult.locations.first()))
                                            locationSubscriber.onComplete()
                                            mLocationProviderClient.removeLocationUpdates(this)
                                        }
                                    },
                                    null)
                        }

                        task.addOnFailureListener { exception ->
                            if (exception is ResolvableApiException) {
                                if (mContext is Activity) {
                                    try {
                                        mWaitingObservers.add(locationSubscriber)
                                        exception.startResolutionForResult(mContext as Activity, mPermissions.LocationRequestCode())
                                    } catch (sendEx: IntentSender.SendIntentException) {
                                        locationSubscriber.onNext(LocationResultImpl(null))
                                        locationSubscriber.onComplete()
                                    }
                                } else {
                                    locationSubscriber.onNext(LocationResultImpl(null))
                                    locationSubscriber.onComplete()
                                }
                            }
                            else {
                                locationSubscriber.onNext(LocationResultImpl(null))
                                locationSubscriber.onComplete()
                            }
                        }

                    } else {
                        locationSubscriber.onNext(LocationResultImpl(null))
                        locationSubscriber.onComplete()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext(false)
                }
            })

        }).subscribeOn(Schedulers.computation())
    }


    override fun RegisterGpsResult(inResultCode: Int) {
        synchronized(mWaitingObservers)
        {
            while (mWaitingObservers.size > 0) {
                if (inResultCode == Activity.RESULT_OK) {
                    val locationRequest = LocationRequest().apply {
                        interval = 1000
                        fastestInterval = 1000
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    }

                    val locationSubscriber = mWaitingObservers[0]

                    if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationProviderClient.requestLocationUpdates(locationRequest,
                                object : LocationCallback() {
                                    override


                                    fun onLocationResult(locationResult: LocationResult?) {
                                        if (locationResult == null) {
                                            locationSubscriber.onNext(LocationResultImpl(null))
                                            locationSubscriber.onComplete()
                                            return
                                        }

                                        locationSubscriber.onNext(LocationResultImpl(locationResult.locations.first()))
                                        locationSubscriber.onComplete()
                                        mLocationProviderClient.removeLocationUpdates(this)
                                    }
                                },
                                null)
                    }
                } else {
                    mWaitingObservers[0].onNext(LocationResultImpl(null))
                    mWaitingObservers[0].onComplete()
                }

                mWaitingObservers.removeAt(0)
            }
        }
    }


    class LocationResultImpl(val mLocation: Location?): LocationProvider.LocationResult
    {
        override fun LocationResult(): Location? {
            return mLocation
        }
    }
}