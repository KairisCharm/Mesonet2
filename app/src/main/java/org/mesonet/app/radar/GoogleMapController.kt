package org.mesonet.app.radar


import android.content.Context
import android.graphics.Bitmap
import android.os.Handler

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng

import org.mesonet.app.dependencyinjection.PerFragment

import java.util.ArrayList
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


@PerFragment
class GoogleMapController @Inject
constructor(internal var mRadarDataController: RadarDataController) : GoogleMapListener, Observer {
    private var mTransparency = 0f

    internal var mPlayPauseState = PlayPauseState()

    @Inject
    lateinit var mRadarSiteDataProvider: RadarSiteDataProvider

    @Inject
    lateinit var mGreatCircleFormulas: VincentysFormulae


    private var mMapProvider: GoogleMapProvider? = null

    private val mPlayHandler = Handler()


    private val mGroundOverlays = ArrayList<GroundOverlay?>(6)

    private var mSelectedImage = 0


    init {
        mRadarDataController.addObserver(this)
    }


    fun SetProvider(inMapProvider: GoogleMapProvider?) {
        mMapProvider = inMapProvider

        UpdateRadarImage()
    }


    fun GetTransparency(): Float {
        return mTransparency
    }


    fun SetTransparency(inTransparency: Float) {
        mTransparency = inTransparency
        SetVisibleImage(mSelectedImage)
    }


    fun GetOverlays(inContext: Context?, inMap: GoogleMap?) {
        val radarDetails = mRadarSiteDataProvider!!.GetRadarDetail()
        val radarImages = mRadarDataController.GetRadarImageDetails()

        if (radarImages != null && inMap != null && inContext != null) {
            inMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(radarDetails.GetLatitude().toDouble(), radarDetails.GetLongitude().toDouble()), 7f)))

            for (i in radarImages.indices) {
                radarImages[i].GetImage(inContext, object : BaseRadarDataController.ImageLoadedListener {
                    override fun BitmapLoaded(inResult: Bitmap) {
                        if (inResult != null) {
                            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(inResult)

                            var transparency = mTransparency
                            if (i != mSelectedImage) {
                                transparency = 1f
                            }

                            val position = LatLng(radarDetails.GetLatitude().toDouble(), radarDetails.GetLongitude().toDouble())

                            val halfLatDelta = (radarDetails.GetLatDelta() / 4).toDouble()
                            val halfLonDelta = (radarDetails.GetLonDelta() / 4).toDouble()

                            val v = mGreatCircleFormulas!!.LatLonDegreesToDistance(radarDetails.GetLatitude().toDouble(), radarDetails.GetLatitude().toDouble(), radarDetails.GetLongitude() - halfLonDelta, radarDetails.GetLongitude() + halfLonDelta)
                            val v1 = mGreatCircleFormulas!!.LatLonDegreesToDistance(radarDetails.GetLatitude() - halfLatDelta, radarDetails.GetLatitude() + halfLatDelta, radarDetails.GetLongitude().toDouble(), radarDetails.GetLongitude().toDouble())

                            if (i >= mGroundOverlays.size || mGroundOverlays[i] == null) {
                                val options = GroundOverlayOptions().image(bitmapDescriptor)
                                        .position(position, radarDetails.GetRange())
                                        .transparency(transparency)

                                while (i >= mGroundOverlays.size)
                                    mGroundOverlays.add(null)

                                mGroundOverlays[i] = inMap.addGroundOverlay(options)
                            } else {
                                mGroundOverlays[i]?.setImage(bitmapDescriptor)
                                mGroundOverlays[i]!!.position = position
                                mGroundOverlays[i]?.setDimensions(radarDetails.GetRange())
                                mGroundOverlays[i]!!.transparency = transparency
                            }

                            inResult.recycle()
                        } else {
                            mGroundOverlays[i]?.setImage(null!!)
                        }
                    }
                })
            }

            if (mMapProvider != null && radarImages.size > mSelectedImage)
                mMapProvider!!.SetTime(radarImages[mSelectedImage].GetTimeString())

            //            Play();
        }
    }


    fun GetRadarName(): String {
        return mRadarSiteDataProvider!!.GetRadarName()
    }


    private fun UpdateRadarImage() {
        if (mMapProvider != null) {
            mMapProvider!!.GetMap(this)
        }
    }


    fun GetPlayPauseState(): PlayPauseState {
        return mPlayPauseState
    }


    fun TogglePlay() {
        mPlayPauseState.SetPlayState(!mPlayPauseState.GetIsPlaying())

        if (mPlayPauseState.mIsPlaying) {
            mPlayHandler.postDelayed(object : Runnable {
                override fun run() {
                    if (mPlayPauseState.GetIsPlaying()) {
                        var newIndex = mSelectedImage - 1

                        if (newIndex < 0)
                            newIndex = mGroundOverlays.size - 1

                        SetVisibleImage(newIndex)
                        mPlayHandler.postDelayed(this, 500)
                    }
                }
            }, 500)
        } else {
            SetVisibleImage(0)
        }
    }


    private fun SetVisibleImage(inIndex: Int) {
        mSelectedImage = inIndex

        if (mSelectedImage < 0 || mSelectedImage >= mGroundOverlays.size)
            mSelectedImage = 0

        for (i in mGroundOverlays.indices) {
            if (mGroundOverlays[i] != null) {
                var transparency = 1f
                if (i == mSelectedImage)
                    transparency = mTransparency

                mGroundOverlays[i]!!.transparency = transparency
            }
        }

        mMapProvider!!.SetTime(mRadarDataController.GetRadarImageDetails()?.get(mSelectedImage)?.GetTimeString())
    }


    override fun update(observable: Observable, o: Any?) {
        UpdateRadarImage()
    }


    override fun MapFound(inContext: Context, inMap: GoogleMap) {
        GetOverlays(inContext, inMap)
    }


    inner class PlayPauseState : Observable() {
        internal var mIsPlaying: Boolean = false


        internal fun SetPlayState(inIsPlaying: Boolean) {
            mIsPlaying = inIsPlaying

            setChanged()
            notifyObservers()
        }


        override fun addObserver(inObserver: Observer) {
            super.addObserver(inObserver)
            setChanged()
            notifyObservers()
        }


        fun GetIsPlaying(): Boolean {
            return mIsPlaying
        }
    }


    interface GoogleMapProvider {
        fun GetMap(inListener: GoogleMapListener)
        fun SetTime(inTimeString: String?)
    }
}
