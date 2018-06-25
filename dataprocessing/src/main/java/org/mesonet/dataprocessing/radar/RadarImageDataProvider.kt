package org.mesonet.dataprocessing.radar

import android.content.Context

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable


interface RadarImageDataProvider {
    fun GetImages(inContext: Context): Observable<GoogleMapController.ImageInfo>
    fun GetLocation(): Observable<LatLng>
}