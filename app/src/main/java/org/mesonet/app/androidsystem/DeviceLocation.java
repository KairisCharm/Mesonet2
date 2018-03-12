package org.mesonet.app.androidsystem;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.dependencyinjection.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;



@PerActivity
public class DeviceLocation
{
    BaseActivity mActivity;

    @Inject
    Permissions mPermissions;

    private List<LocationListener> mLocationListeners = new ArrayList<>();

    @Inject
    public DeviceLocation(BaseActivity inActivity)
    {
        mActivity = inActivity;
    }



    public void GetLocation(LocationListener inLocationListener)
    {
        mLocationListeners.add(inLocationListener);

        mPermissions.RequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, new Permissions.PermissionListener() {
            @Override
            public Activity GetActivity() {
                return mActivity;
            }

            @Override
            public void PermissionGranted() {
                if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.getFusedLocationProviderClient(mActivity).requestLocationUpdates(LocationRequest.create(), new LocationCallback() {
                        public void onLocationResult(LocationResult inResult) {
                            Location result = inResult.getLastLocation();

                            if (result == null)
                                LocationUnavailable();

                            else
                                LocationFound(result);

                            LocationServices.getFusedLocationProviderClient(mActivity).removeLocationUpdates(this);
                        }
                    }, null);
                }
            }

            @Override
            public void PermissionDenied() {
                LocationUnavailable();
            }
        });
    }



    private void LocationFound(Location inLocation)
    {
        for(int i = 0; i < mLocationListeners.size();)
        {
            mLocationListeners.get(i).LastLocationFound(inLocation);
            mLocationListeners.remove(i);
        }
    }



    private void LocationUnavailable()
    {
        for(int i = 0; i < mLocationListeners.size();)
        {
            mLocationListeners.get(i).LocationUnavailable();
            mLocationListeners.remove(i);
        }
    }



    public interface LocationListener
    {
        void LastLocationFound(Location inLocation);
        void LocationUnavailable();
    }
}
