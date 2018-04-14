package org.mesonet.app.radar

import android.content.Context

import com.google.android.gms.maps.GoogleMap


interface GoogleMapListener {
    fun MapFound(inContext: Context, inMap: GoogleMap)
}
