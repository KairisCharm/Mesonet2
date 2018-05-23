package org.mesonet.dataprocessing.radar


import android.content.Context
import android.graphics.Bitmap
import android.os.Handler

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import org.mesonet.core.ThreadHandler

import java.util.Observable
import java.util.Observer

import javax.inject.Inject


@org.mesonet.core.PerFragment
class GoogleMapController @Inject
constructor(internal var mRadarDataController: RadarDataController) : RadarImageDataProvider, Observer {
    private var mTransparency = 0f

    private var mPlayPauseState = PlayPauseState()

    @Inject
    internal lateinit var mRadarSiteDataProvider: RadarSiteDataProvider

    @Inject
    internal lateinit var mThreadHandler: ThreadHandler

    private var mRadarName: String? = null


    private var mMapProvider: GoogleMapSetup? = null

    private val mPlayHandler = Handler()

    private var mSelectedImage = 0



    init {
        mRadarDataController.addObserver(this)
    }


    fun SetProvider(inMapProvider: GoogleMapSetup?) {
        mThreadHandler.Run("RadarData", Runnable {
            mMapProvider = inMapProvider

            UpdateRadarImage()
        })
    }


    fun GetTransparency(): Float {
        return mTransparency
    }


    fun SetTransparency(inTransparency: Float) {
        mThreadHandler.Run("RadarData", Runnable {
            mTransparency = inTransparency
            SetVisibleImage(mSelectedImage)
        })
    }


    internal fun GetOverlays(inContext: Context, inListener: RadarImageDataProvider.RadarDataListener) {
        val radarDetails = mRadarSiteDataProvider.GetRadarDetail()
        val radarImages = mRadarDataController.GetRadarImageDetails()

        if (radarImages != null) {
            for (i in radarImages.indices) {
                mRadarDataController.GetImage(i, inContext, object : RadarDataController.ImageLoadedListener {
                    override fun BitmapLoaded(inResult: Bitmap) {

                        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(inResult)

                        var transparency = mTransparency
                        if (i != mSelectedImage) {
                            transparency = 1f
                        }

                        val position = LatLng(radarDetails.GetLatitude()!!.toDouble(), radarDetails.GetLongitude()!!.toDouble())

                        val options = GroundOverlayOptions().image(bitmapDescriptor)
                                .position(position, radarDetails.GetRange()!!)
                                .transparency(transparency)

                        inResult.recycle()

                        inListener.FoundImage(options, i, transparency)

                        if(i == (RadarDataController.kRadarImageLimit - 1))
                            SetVisibleImage(mSelectedImage)
                    }
                })
            }
        }
    }


    fun GetRadarName(): String? {
        return mRadarName
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
        mThreadHandler.Run("RadarData", Runnable {
            mPlayPauseState.SetPlayState(!mPlayPauseState.GetIsPlaying())

            if (mPlayPauseState.mIsPlaying) {
                mPlayHandler.postDelayed(object : Runnable {
                    override fun run() {
                        if (mPlayPauseState.GetIsPlaying()) {
                            var newIndex = mSelectedImage - 1

                            if (newIndex < 0)
                                newIndex = RadarDataController.kRadarImageLimit - 1

                            SetVisibleImage(newIndex)
                            mPlayHandler.postDelayed(this, 500)
                        }
                    }
                }, 500)
            } else {
                SetVisibleImage(0)
            }
        })
    }


    private fun SetVisibleImage(inIndex: Int) {
        mSelectedImage = inIndex

        if (mSelectedImage < 0 || mSelectedImage >= RadarDataController.kRadarImageLimit)
            mSelectedImage = 0

        mMapProvider?.SetActiveImage(inIndex, mTransparency, mRadarDataController.GetRadarImageDetails()?.get(mSelectedImage)!!.GetTimeString())
    }


    override fun update(observable: Observable, o: Any?) {
        mThreadHandler.Run("RadarData", Runnable {
            mRadarName = mRadarSiteDataProvider.GetRadarName()
            UpdateRadarImage()
        })
    }


    override fun GetImages(inContext: Context, inListener: RadarImageDataProvider.RadarDataListener) {
        mThreadHandler.Run("RadarData", Runnable {
            GetOverlays(inContext, inListener)
        })
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


    override fun GetLocation(inListener: RadarImageDataProvider.RadarDataListener) {
        var result: LatLng? = null
        mThreadHandler.Run("RadarData", Runnable {
            val radarDetails = mRadarSiteDataProvider.GetRadarDetail()

            result = LatLng(radarDetails.GetLatitude()!!.toDouble(), radarDetails.GetLongitude()!!.toDouble())
        }, Runnable {
            if(result != null)
                inListener.FoundLatLng(result!!)
        })
    }

    fun StartUpdates()
    {
        mThreadHandler.Run("RadarData", Runnable {
            mRadarDataController.StartUpdates()
        })
    }


    fun StopUpdates()
    {
        mThreadHandler.Run("RadarData", Runnable {
            mRadarDataController.StopUpdates()
        })
    }


    interface GoogleMapSetup {
        fun GetMap(inListener: RadarImageDataProvider)
        fun SetActiveImage(inIndex: Int, inTransparency: Float, inTimeString: String?)
    }
}
