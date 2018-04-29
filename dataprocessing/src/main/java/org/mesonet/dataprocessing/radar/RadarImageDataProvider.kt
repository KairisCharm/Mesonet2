package org.mesonet.dataprocessing.radar

import android.content.Context
import com.google.android.gms.maps.model.GroundOverlayOptions

import com.google.android.gms.maps.model.LatLng


interface RadarImageDataProvider {
    fun GetImages(inContext: Context, inListener: RadarDataListener)
    fun GetLocation(inListener: RadarDataListener)



    interface RadarDataListener
    {
        fun FoundImage(inRadarImage: GroundOverlayOptions, inIndex: Int, inTransparency: Float)
        fun FoundLatLng(inLatLng: LatLng)
    }
}
