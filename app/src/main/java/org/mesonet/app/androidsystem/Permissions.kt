package org.mesonet.app.androidsystem


import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Permissions @Inject
constructor() {
    private val mPermissionsListeners = HashMap<String, MutableList<PermissionListener>>()


    fun RequestPermission(inPermission: String, inListener: PermissionListener) {
        if (!mPermissionsListeners.containsKey(inPermission))
            mPermissionsListeners[inPermission] = ArrayList()

        if (!mPermissionsListeners[inPermission]?.contains(inListener)!!)
            mPermissionsListeners[inPermission]?.add(inListener)

        if (ContextCompat.checkSelfPermission(inListener.GetActivity(), inPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(inListener.GetActivity(),
                    arrayOf(inPermission),
                    0)
        } else {
            inListener.PermissionGranted()
        }
    }


    fun ProcessPermissionResponse(inPermissions: Array<String>, inGrantResults: IntArray) {
        for (i in inPermissions.indices) {
            if (mPermissionsListeners.containsKey(inPermissions[i])) {
                val permissionListeners = mPermissionsListeners[inPermissions[i]]

                val j = 0
                if (permissionListeners != null) {
                    while (j < permissionListeners.size) {
                        if (inGrantResults[i] == PackageManager.PERMISSION_GRANTED)
                            permissionListeners.get(j).PermissionGranted()
                        else
                            permissionListeners.get(j).PermissionDenied()

                        permissionListeners.removeAt(j)
                    }
                }
            }
        }
    }


    interface PermissionListener {
        fun GetActivity(): Activity
        fun PermissionGranted()
        fun PermissionDenied()
    }
}
