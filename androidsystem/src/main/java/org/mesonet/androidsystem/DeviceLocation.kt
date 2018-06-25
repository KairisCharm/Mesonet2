package org.mesonet.androidsystem


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler

import org.mesonet.dataprocessing.LocationProvider
import android.content.Context

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceLocation @Inject
constructor(internal var mContext: Context, internal var mThreadHandler: ThreadHandler) : LocationProvider {

  @Inject
  internal lateinit var mPermissions: Permissions

  private val mLocationListeners = ArrayList<LocationProvider.LocationListener>()


  override fun GetLocation(inLocationListener: LocationProvider.LocationListener) {
    mThreadHandler.Run("AndroidSystem", Runnable {
      synchronized(DeviceLocation@this)
      {
        mLocationListeners.add(inLocationListener)
      }

      mPermissions.RequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, object : Permissions.PermissionListener {
        override fun GetContext(): Context {
          return mContext
        }

        override fun PermissionGranted() {
          if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(mContext).requestLocationUpdates(LocationRequest.create(), object : LocationCallback() {
              override fun onLocationResult(inResult: LocationResult?) {
                val result = inResult!!.lastLocation

                if (result == null)
                  LocationUnavailable()
                else
                  LocationFound(result)

                LocationServices.getFusedLocationProviderClient(mContext).removeLocationUpdates(this)
              }
            }, null)
          }
        }

        override fun PermissionDenied() {
          LocationUnavailable()
        }
      })
    })
  }


  private fun LocationFound(inLocation: Location?) {
    synchronized(DeviceLocation@this)
    {
      val i = 0
      while (i < mLocationListeners.size) {
        mLocationListeners[i].LastLocationFound(inLocation)
        mLocationListeners.removeAt(i)
      }
    }
  }


  private fun LocationUnavailable() {
    synchronized(DeviceLocation@this)
    {
      val i = 0
      while (i < mLocationListeners.size) {
        mLocationListeners[i].LocationUnavailable()
        mLocationListeners.removeAt(i)
      }
    }
  }
}