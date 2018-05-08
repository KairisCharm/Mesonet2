package org.mesonet.app.radar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.RadarFragmentBinding
import org.mesonet.app.filterlist.FilterListFragment
import org.mesonet.dataprocessing.radar.GoogleMapController
import org.mesonet.dataprocessing.radar.RadarImageDataProvider
import java.util.ArrayList

import javax.inject.Inject


class RadarFragment : BaseFragment(), GoogleMapController.GoogleMapSetup, FilterListFragment.FilterListCloser {

    @Inject
    internal lateinit var mMapController: GoogleMapController

    private val mGroundOverlays = ArrayList<GroundOverlay?>(6)


    private var mBinding: RadarFragmentBinding? = null

    private var mSnackbar: Snackbar? = null
    private var mLastLocation: LatLng? = null


    private var mBindingReadyListener: BindingReadyListener? = null


    override fun onCreate(inSavedInstanceState: Bundle?) {
        super.onCreate(inSavedInstanceState)

        mMapController.SetProvider(this)
    }


    override fun onActivityCreated(inSavedInstanceState: Bundle?) {
        super.onActivityCreated(inSavedInstanceState)

        mBinding!!.map.onCreate(inSavedInstanceState)
    }


    override fun onCreateView(inInflater: LayoutInflater, inContainer: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.radar_fragment, inContainer, false)

        if (mBindingReadyListener != null)
            mBindingReadyListener!!.BindingReady(mBinding)

        mBindingReadyListener = null

        mSnackbar = Snackbar.make(mBinding!!.radarLayout, "", Snackbar.LENGTH_INDEFINITE).setAction("Change") {
            val transaction = getChildFragmentManager().beginTransaction()
            transaction.replace(R.id.childFragmentContainer, FilterListFragment())
            transaction.commit()
            RevealView(mBinding!!.radarLayout)
            mBinding!!.playPauseButton.hide()
        }

        mBinding!!.playPauseButton.SetPlayPauseState(mMapController.GetPlayPauseState())
        mBinding!!.playPauseButton.setOnClickListener { mMapController.TogglePlay() }

        mBinding!!.transparencySeekBar.max = 255
        mBinding!!.transparencySeekBar.progress = Math.round(255.0f * (1 - mMapController.GetTransparency()))
        mBinding!!.transparencySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(inSeekBar: SeekBar, inProgress: Int, inFromUser: Boolean) {
                mMapController.SetTransparency(1 - inProgress / 255.0f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        return mBinding!!.root
    }


    override fun onResume() {
        super.onResume()

        mBinding!!.map.onResume()
        mMapController.StartUpdates()
    }


    override fun onPause() {
        mBinding!!.map.onPause()
        mMapController.StopUpdates()

        super.onPause()
    }


    override fun onDestroy() {
        mMapController.SetProvider(null)
        mBinding!!.map.onDestroy()

        mMapController.StopUpdates()

        super.onDestroy()
    }


    override fun onSaveInstanceState(inSaveInstanceState: Bundle) {
        super.onSaveInstanceState(inSaveInstanceState)

        mBinding!!.map.onSaveInstanceState(inSaveInstanceState)
    }


    override fun onLowMemory() {
        super.onLowMemory()

        mBinding!!.map.onLowMemory()
    }


    override fun GetMap(inListener: RadarImageDataProvider) {
        if(activity != null && isAdded()) {
            activity?.runOnUiThread({
                val bindingReadyListener = object : BindingReadyListener {
                    override fun BindingReady(inBinding: RadarFragmentBinding?) {
                        if (inBinding != null) {
                            inBinding.map.getMapAsync { inMap ->
                                val listener = object : RadarImageDataProvider.RadarDataListener {
                                    override fun FoundImage(inRadarImage: GroundOverlayOptions, inIndex: Int, inTransparency: Float) {
                                        if(activity != null && isAdded()) {
                                            activity?.runOnUiThread({
                                                while (inIndex >= mGroundOverlays.size)
                                                    mGroundOverlays.add(null)

                                                if (mGroundOverlays[inIndex] == null) {
                                                    mGroundOverlays[inIndex] = inMap.addGroundOverlay(inRadarImage)
                                                } else {
                                                    mGroundOverlays[inIndex]?.setImage(inRadarImage.image)
                                                    mGroundOverlays[inIndex]?.position = inRadarImage.location
                                                    mGroundOverlays[inIndex]?.setDimensions(inRadarImage.width, inRadarImage.height)
                                                }

                                                mGroundOverlays[inIndex]?.transparency = inTransparency
                                            })
                                        }
                                    }

                                    override fun FoundLatLng(inLatLng: LatLng) {
                                        if(mLastLocation == null || !mLastLocation!!.equals(inLatLng) && activity != null) {
                                            mLastLocation = inLatLng
                                            activity?.runOnUiThread({
                                                inMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(inLatLng.latitude, inLatLng.longitude), 7f)))
                                            })
                                        }
                                    }
                                }
                                mMapController.GetLocation(listener)
                                inListener.GetImages(context!!, listener)
                            }
                        }
                    }
                }

                if (mBinding == null)
                    mBindingReadyListener = bindingReadyListener
                else
                    bindingReadyListener.BindingReady(mBinding)
            })
        }
    }

    override fun Close() {
        if(activity != null && isAdded()) {
            activity?.runOnUiThread({
                RevealView(mBinding!!.radarLayout)
                mSnackbar!!.show()
                mBinding!!.playPauseButton.show()
            })
        }
    }


    private fun RevealView(view: View) {
        val cx = view.left + view.right - 24
        val cy = (view.top + view.bottom) / 2
        val radius = Math.max(mBinding!!.childFragmentContainer.width, mBinding!!.childFragmentContainer.height) * 2.0f

        if (mBinding!!.childFragmentContainer.visibility != View.VISIBLE) {
            mBinding!!.childFragmentContainer.visibility = View.VISIBLE
            val hide = ViewAnimationUtils.createCircularReveal(mBinding!!.childFragmentContainer, cx, cy, 0f, radius)
            hide.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding!!.childFragmentContainer.visibility = View.VISIBLE
                    hide.removeListener(this)
                }
            })
            hide.start()

        } else {
            val reveal = ViewAnimationUtils.createCircularReveal(
                    mBinding!!.childFragmentContainer, cx, cy, radius, 0f)
            reveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding!!.childFragmentContainer.visibility = View.INVISIBLE
                    reveal.removeListener(this)
                }
            })
            reveal.start()
        }
    }


    override fun SetActiveImage(inIndex: Int, inTransparency: Float, inTimeString: String?) {
        if(activity != null && isAdded()) {
            activity?.runOnUiThread({
                for (i in mGroundOverlays.indices) {
                    if (mGroundOverlays[i] != null) {
                        var transparency = 1f
                        if (i == inIndex)
                            transparency = inTransparency

                        mGroundOverlays[i]!!.transparency = transparency
                    }
                }

                if (inTimeString != null && !inTimeString.isEmpty()) {
                    var timeString = ""
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                        timeString = "\n"
                    else
                        timeString += ", "

                    timeString += inTimeString
                    mSnackbar!!.setText(mMapController.GetRadarName() + timeString)
                    mSnackbar!!.show()
                    mBinding!!.playPauseButton.show()
                } else {
                    mBinding!!.playPauseButton.hide()
                }
            })
        }
    }


    private interface BindingReadyListener {
        fun BindingReady(inBinding: RadarFragmentBinding?)
    }
}
