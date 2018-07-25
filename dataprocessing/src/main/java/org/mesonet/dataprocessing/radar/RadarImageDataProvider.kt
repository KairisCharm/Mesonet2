package org.mesonet.dataprocessing.radar

import android.content.Context
import android.graphics.Bitmap

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.models.radar.RadarDetails
import org.mesonet.models.radar.RadarHistory


interface RadarImageDataProvider {
    fun GetSiteNameSubject(): BehaviorSubject<String>
    fun GetSiteDetailSubject(): BehaviorSubject<RadarDetails>
    fun GetRadarAnimationObservable(): Observable<List<RadarDataController.ImageSubject>>
    fun Dispose()
}