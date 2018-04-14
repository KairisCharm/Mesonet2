package org.mesonet.app.androidsystem


import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.dependencyinjection.PerActivity

import java.util.ArrayList

import javax.inject.Inject


@PerActivity
class DeviceLocation @Inject
constructor(internal var mActivity: BaseActivity) {

    @Inject
    lateinit var mPermissions: Permissions

    private val mLocationListeners = ArrayList<LocationListener>()


    fun GetLocation(inLocationListener: LocationListener) {
        mLocationListeners.add(inLocationListener)

        mPermissions!!.RequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, object : Permissions.PermissionListener {
            override fun GetActivity(): Activity {
                return mActivity
            }

            override fun PermissionGranted() {
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.getFusedLocationProviderClient(mActivity).requestLocationUpdates(LocationRequest.create(), object : LocationCallback() {
                        override fun onLocationResult(inResult: LocationResult?) {
                            val result = inResult!!.lastLocation

                            if (result == null)
                                LocationUnavailable()
                            else
                                LocationFound(result)

                            LocationServices.getFusedLocationProviderClient(mActivity).removeLocationUpdates(this)
                        }
                    }, null)
                }
            }

            override fun PermissionDenied() {
                LocationUnavailable()
            }
        })
    }


    private fun LocationFound(inLocation: Location?) {
        val i = 0
        while (i < mLocationListeners.size) {
            mLocationListeners[i].LastLocationFound(inLocation)
            mLocationListeners.removeAt(i)
        }
    }


    private fun LocationUnavailable() {
        val i = 0
        while (i < mLocationListeners.size) {
            mLocationListeners[i].LocationUnavailable()
            mLocationListeners.removeAt(i)
        }
    }


    interface LocationListener {
        fun LastLocationFound(inLocation: Location?)
        fun LocationUnavailable()
    }
}
