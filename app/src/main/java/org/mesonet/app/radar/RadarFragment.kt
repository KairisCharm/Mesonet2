package org.mesonet.app.radar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.SeekBar
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngQuad
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.ImageSource
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.RadarFragmentBinding
import org.mesonet.app.filterlist.FilterListFragment
import org.mesonet.dataprocessing.radar.MapboxMapController
import org.mesonet.dataprocessing.radar.RadarDataController
import org.mesonet.dataprocessing.radar.RadarImageDataProvider
import org.mesonet.models.radar.RadarDetails
import java.util.concurrent.TimeUnit

import javax.inject.Inject


class RadarFragment : BaseFragment(), FilterListFragment.FilterListCloser {

    private val kRasterImageName = "Radar"

    @Inject
    internal lateinit var mRadarImageDataProvider: RadarImageDataProvider

    @Inject
    internal lateinit var mMapController: MapboxMapController

    private var mRadarImageDisposable: Disposable? = null


    private var mBinding: RadarFragmentBinding? = null

    private var mSnackbar: Snackbar? = null

    private var mMapFragment: SupportMapFragment? = null

    private var mRadarLayer: RasterLayer? = null
    private var mRadarImageSource: ImageSource? = null


    override fun onCreateView(inInflater: LayoutInflater, inContainer: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.radar_fragment, inContainer, false)

        val options = MapboxMapOptions()
        options.styleUrl("mapbox://styles/okmesonet/cjic5xvsd02xl2sp7sh1eljuj")

        mMapFragment = SupportMapFragment.newInstance(options)

        mRadarImageSource = ImageSource(kRasterImageName, LatLngQuad(LatLng(0.0, 0.0),
                                                                     LatLng(0.0, 0.0),
                                                                     LatLng(0.0, 0.0),
                                                                     LatLng(0.0, 0.0)), R.drawable.ic_close_white_36dp)



        mRadarLayer = RasterLayer(kRasterImageName, mRadarImageSource?.id)

        val mapTransaction = childFragmentManager.beginTransaction()
        mapTransaction.add(R.id.mapContainer, mMapFragment ?: Fragment())
        mapTransaction.commit()

        mBinding?.playPauseButton?.SetPlayPauseState(mMapController.GetPlayPauseStateObservable())

        mBinding?.playPauseButton?.setOnClickListener {
            mMapController.TogglePlay()
        }

        mSnackbar = Snackbar.make(mBinding?.radarLayout ?: View(context), "", Snackbar.LENGTH_INDEFINITE).setAction("Change") {
            val filterTransaction = childFragmentManager.beginTransaction()
            filterTransaction.replace(R.id.childFragmentContainer, FilterListFragment())
            filterTransaction.commit()
            RevealView(mBinding?.radarLayout)
            mBinding?.playPauseButton?.hide()
        }

        mBinding?.transparencySeekBar?.max = 255
        mBinding?.transparencySeekBar?.progress = Math.round(mMapController.GetTransparencySubject().value * 255.0f)
        mMapController.GetTransparencySubject().observeOn(AndroidSchedulers.mainThread()).subscribe (object: Observer<Float> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: Float) {
                mRadarLayer?.setProperties(PropertyFactory.rasterOpacity(t))
            }
        })

        mBinding?.transparencySeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(inSeekBar: SeekBar, inProgress: Int, inFromUser: Boolean) {
                val result = inProgress / 255.0f

                val subject = mMapController.GetTransparencySubject()

                if(!subject.hasValue() || subject.value != result)
                    mMapController.GetTransparencySubject().onNext(result)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        return mBinding?.root?: View(context)
    }


    override fun onResume() {
        super.onResume()

        mRadarImageDataProvider.GetSiteNameSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<String>
        {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()

                SetSnackbarText("Error", "", "")
            }

            override fun onNext(radarId: String) {
                mRadarImageDataProvider.GetSiteDetailSubject().observeOn(AndroidSchedulers.mainThread()).subscribe (object: Observer<RadarDetails>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()

                        SetSnackbarText("Error", "", "")
                    }

                    override fun onNext(radarDetails: RadarDetails) {
                        SetSnackbarText(radarId, radarDetails.GetName(), "")
                        mSnackbar?.show()
                        mRadarLayer?.setProperties(PropertyFactory.rasterOpacity(0.0f))
                        mMapFragment?.getMapAsync { map ->
                            val sourceId = mRadarImageSource?.id ?: ""
                            val layerId = mRadarLayer?.id ?: ""
                            if (sourceId.isNotEmpty() && map.getSource(sourceId) == null)
                                map.addSource(mRadarImageSource?: ImageSource(0))
                            if (radarId.isNotEmpty() && map.getLayer(layerId) == null)
                                map.addLayer(mRadarLayer?: RasterLayer(0))
                            mRadarImageSource?.setCoordinates(LatLngQuad(LatLng(radarDetails.GetNorthEastCorner()?.GetLatitude()?.toDouble()
                                    ?: 0.0, radarDetails.GetSouthWestCorner()?.GetLongitude()?.toDouble()
                                    ?: 0.0),
                                    LatLng(radarDetails.GetNorthEastCorner()?.GetLatitude()?.toDouble()
                                            ?: 0.0, radarDetails.GetNorthEastCorner()?.GetLongitude()?.toDouble()
                                            ?: 0.0),
                                    LatLng(radarDetails.GetSouthWestCorner()?.GetLatitude()?.toDouble()
                                            ?: 0.0, radarDetails.GetNorthEastCorner()?.GetLongitude()?.toDouble()
                                            ?: 0.0),
                                    LatLng(radarDetails.GetSouthWestCorner()?.GetLatitude()?.toDouble()
                                            ?: 0.0, radarDetails.GetSouthWestCorner()?.GetLongitude()?.toDouble()
                                            ?: 0.0)))
                            mMapController.GetCameraPositionObservable(radarDetails.GetLatitude()?.toDouble()
                                    ?: 0.0, radarDetails.GetLongitude()?.toDouble()
                                    ?: 0.0, 5.0).observeOn(AndroidSchedulers.mainThread()).subscribe (object: Observer<CameraPosition>{
                                override fun onComplete() {

                                }

                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onNext(t: CameraPosition)
                                {
                                    map.cameraPosition = t
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }

                            })

                            mRadarImageDataProvider.GetRadarAnimationObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<List<RadarDataController.ImageSubject>>{
                                override fun onComplete() {

                                }

                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onNext(imageList: List<RadarDataController.ImageSubject>)
                                {
                                    mMapController.SetFrameCount(imageList.size)

                                    mBinding?.playPauseButton?.visibility = View.VISIBLE

                                    mMapController.GetActiveImageIndexObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<Int>{
                                        override fun onComplete() {

                                        }

                                        override fun onSubscribe(d: Disposable) {

                                        }

                                        override fun onNext(index: Int)
                                        {
                                            mRadarImageDisposable?.dispose()
                                            imageList[index].GetSubject().observeOn(AndroidSchedulers.mainThread()).subscribe (object: Observer<Bitmap>{
                                                override fun onComplete() {

                                                }

                                                override fun onSubscribe(d: Disposable) {
                                                    mRadarImageDisposable = d
                                                }

                                                override fun onNext(t: Bitmap) {
                                                    if (!t.isRecycled) {
                                                        mRadarImageSource?.setImage(t)

                                                        mMapController.GetTransparencySubject().observeOn(AndroidSchedulers.mainThread()).subscribe (object: Observer<Float>
                                                        {
                                                            override fun onComplete() {

                                                            }

                                                            override fun onSubscribe(d: Disposable) {

                                                            }

                                                            override fun onNext(t: Float)
                                                            {
                                                                mRadarLayer?.setProperties(PropertyFactory.rasterOpacity(t))
                                                            }

                                                            override fun onError(e: Throwable) {
                                                                onNext(0.0f)
                                                                e.printStackTrace()
                                                            }

                                                        })

                                                        SetSnackbarText(radarId, radarDetails.GetName(), imageList[index].GetTimestring())
                                                    }
                                                }

                                                override fun onError(e: Throwable) {
                                                    SetSnackbarText(radarId, radarDetails.GetName(), "Error")
                                                    e.printStackTrace()
                                                }

                                            })
                                        }

                                        override fun onError(e: Throwable) {
                                            SetSnackbarText(radarId, radarDetails.GetName(), "Error")
                                            e.printStackTrace()
                                        }

                                    })
                                }

                                override fun onError(e: Throwable) {
                                    SetSnackbarText(radarId, radarDetails.GetName(), "Error")
                                    e.printStackTrace()
                                }
                            })
                        }
                    }
                })
            }
        })
    }


    override fun onPause() {
        mRadarImageDisposable?.dispose()

        super.onPause()
    }


    override fun Close() {
        Observable.create(ObservableOnSubscribe<Void>{
            RevealView(mBinding?.radarLayout)
            mSnackbar?.show()
            mBinding?.playPauseButton?.show()
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }


    private fun RevealView(view: View?) {
        val cx = view?.left ?: 0 + (view?.right ?: 0) - 24
        val cy = (view?.top ?: 0 + (view?.bottom ?: 0)) / 2
        val radius = Math.max(mBinding?.childFragmentContainer?.width ?: 0, mBinding?.childFragmentContainer?.height ?: 0) * 2.0f

        if (mBinding?.childFragmentContainer?.visibility != View.VISIBLE) {
            mBinding?.childFragmentContainer?.visibility = View.VISIBLE
            val hide = ViewAnimationUtils.createCircularReveal(mBinding?.childFragmentContainer, cx, cy, 0f, radius)
            hide.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding?.childFragmentContainer?.visibility = View.VISIBLE
                    hide.removeListener(this)
                }
            })
            hide.start()

        } else {
            val reveal = ViewAnimationUtils.createCircularReveal(
                    mBinding?.childFragmentContainer, cx, cy, radius, 0f)
            reveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding?.childFragmentContainer?.visibility = View.INVISIBLE
                    reveal.removeListener(this)
                }
            })
            reveal.start()
        }
    }



    internal fun SetSnackbarText(inRadarId: String?, inRadarName: String?, inTimeString: String?)
    {
        var timeString = ""

        if(inTimeString != null) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                timeString = "\n"
            else
                timeString += ", "
        }

        timeString += inTimeString?: ""

        val radarId = inRadarId ?: ""
        val radarName = inRadarName ?: ""

        mSnackbar?.setText("$radarId - $radarName$timeString")
    }
}
