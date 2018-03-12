package org.mesonet.app.androidsystem;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Permissions
{
    private Map<String, List<PermissionListener>> mPermissionsListeners = new HashMap<>();


    @Inject
    public Permissions(){}



    public void RequestPermission(String inPermission, PermissionListener inListener)
    {
        if(!mPermissionsListeners.containsKey(inPermission))
            mPermissionsListeners.put(inPermission, new ArrayList<PermissionListener>());

        if(!mPermissionsListeners.get(inPermission).contains(inListener))
            mPermissionsListeners.get(inPermission).add(inListener);

        if (ContextCompat.checkSelfPermission(inListener.GetActivity(), inPermission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(inListener.GetActivity(),
                    new String[]{inPermission},
                    0);
        }
        else
        {
            inListener.PermissionGranted();
        }
    }



    public void ProcessPermissionResponse(String[] inPermissions, int[] inGrantResults)
    {
        for(int i = 0; i < inPermissions.length; i++)
        {
            if(mPermissionsListeners.containsKey(inPermissions[i]))
            {
                List<PermissionListener> permissionListeners = mPermissionsListeners.get(inPermissions[i]);

                for(int j = 0; j < permissionListeners.size(); )
                {
                    if(inGrantResults[i] == PackageManager.PERMISSION_GRANTED)
                        permissionListeners.get(j).PermissionGranted();
                    else
                        permissionListeners.get(j).PermissionDenied();

                    permissionListeners.remove(j);
                }
            }
        }
    }



    public interface PermissionListener
    {
        Activity GetActivity();
        void PermissionGranted();
        void PermissionDenied();
    }
}
