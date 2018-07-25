package org.mesonet.androidsystem

import android.content.Context
import io.reactivex.Observable

interface Permissions
{
    fun LocationRequestCode(): Int
    fun RequestPermission(inContext: Context?, inPermission: String): Observable<Boolean>
    fun ProcessPermissionResponse(inPermissions: Array<String>, inGrantResults: IntArray)
}