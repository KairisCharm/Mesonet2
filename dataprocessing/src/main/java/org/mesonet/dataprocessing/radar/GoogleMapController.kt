package org.mesonet.dataprocessing.radar

import android.content.Context
import android.os.Handler

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject



@org.mesonet.core.PerFragment
class GoogleMapController @Inject
constructor(internal var mRadarDataController: RadarDataController) : RadarImageDataProvider, Observer<Void> {

    private var mTransparency = 0f

    private var mPlayPauseState = PlayPauseState()

    @Inject
    internal lateinit var mRadarSiteDataProvider: RadarSiteDataProvider


    private var mRadarName: String? = null


    private var mMapProvider: GoogleMapSetup? = null

    private val mPlayHandler = Handler()

    private var mSelectedImage = 0



    init {
        mRadarDataController.subscribe(this)
    }


    fun SetProvider(inMapProvider: GoogleMapSetup?) {
        Observable.create(ObservableOnSubscribe<Void>{
            mMapProvider = inMapProvider

            UpdateRadarImage()
        })
    }


    fun GetTransparency(): Float {
        return mTransparency
    }


    fun SetTransparency(inTransparency: Float) {
        Observable.create(ObservableOnSubscribe<Void> {
            mTransparency = inTransparency
            SetVisibleImage(mSelectedImage)
        })
    }


    override fun GetImages(inContext: Context): Observable<ImageInfo> {
        return Observable.create{observer ->
            val radarDetails = mRadarSiteDataProvider.GetRadarDetail()
            val radarImages = mRadarDataController.GetRadarImageDetails()

            for (i in radarImages.indices) {
                mRadarDataController.GetImage(i, inContext).observeOn(Schedulers.computation()).subscribe{

                    val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(it)

                    var transparency = mTransparency
                    if (i != mSelectedImage) {
                        transparency = 1f
                    }

                    val position = LatLng(radarDetails.GetLatitude()!!.toDouble(), radarDetails.GetLongitude()!!.toDouble())

                    val options = GroundOverlayOptions().image(bitmapDescriptor)
                            .position(position, radarDetails.GetRange()!!)
                            .transparency(transparency)

                    it.recycle()

                    observer.onNext(ImageInfoImpl(i, options, transparency))

                    if(i == radarImages.indices.last)
                        SetVisibleImage(mSelectedImage)
                }
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
        Observable.create(ObservableOnSubscribe<Void> {
            mPlayPauseState.SetPlayState(!mPlayPauseState.mIsPlaying)

            if (mPlayPauseState.mIsPlaying) {
                mPlayHandler.postDelayed(object : Runnable {
                    override fun run() {
                        if (mPlayPauseState.mIsPlaying) {
                            var newIndex = mSelectedImage - 1

                            if (newIndex < 0)
                                newIndex = mRadarDataController.GetRadarImageDetails().indices.last

                            SetVisibleImage(newIndex)
                            mPlayHandler.postDelayed(this, 500)
                        }
                    }
                }, 500)
            } else {
                SetVisibleImage(0)
            }
        }).subscribe()
    }


    private fun SetVisibleImage(inIndex: Int) {
        mSelectedImage = inIndex

        if (mSelectedImage < 0 || mSelectedImage > mRadarDataController.GetRadarImageDetails().indices.last)
            mSelectedImage = 0

        mMapProvider?.SetActiveImage(inIndex, mTransparency, mRadarDataController.GetRadarImageDetails()[mSelectedImage].GetTimeString())
    }


    override fun onNext(t: Void) {
        mRadarName = mRadarSiteDataProvider.GetRadarName()
        UpdateRadarImage()
    }

    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}


    inner class PlayPauseState : Observable<Boolean>() {
        internal var mIsPlaying: Boolean = false

        override fun subscribeActual(observer: Observer<in Boolean>?) {
            observer?.onNext(mIsPlaying)
        }


        internal fun SetPlayState(inIsPlaying: Boolean) {
            mIsPlaying = inIsPlaying
        }
    }


    override fun GetLocation(): Observable<LatLng> {
        return Observable.create{
            val radarDetails = mRadarSiteDataProvider.GetRadarDetail()

            it.onNext(LatLng(radarDetails.GetLatitude()!!.toDouble(), radarDetails.GetLongitude()!!.toDouble()))
        }
    }

    fun StartUpdates()
    {
        mRadarDataController.subscribe()
    }


    interface GoogleMapSetup {
        fun GetMap(inListener: RadarImageDataProvider)
        fun SetActiveImage(inIndex: Int, inTransparency: Float, inTimeString: String?)
    }


    class ImageInfoImpl(val mIndex: Int, val mGroundOverlayOptions: GroundOverlayOptions, val mTransparency: Float): ImageInfo {
        override fun GetIndex(): Int {
            return mIndex
        }

        override fun GetOptions(): GroundOverlayOptions {
            return mGroundOverlayOptions
        }

        override fun GetTransparency(): Float {
            return mTransparency
        }
    }

    interface ImageInfo
    {
        fun GetIndex(): Int
        fun GetOptions(): GroundOverlayOptions
        fun GetTransparency(): Float
    }
}