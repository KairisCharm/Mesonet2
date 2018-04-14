package org.mesonet.app.radar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.SeekBar

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.RadarFragmentBinding
import org.mesonet.app.filterlist.FilterListFragment

import javax.inject.Inject


class RadarFragment : BaseFragment(), GoogleMapController.GoogleMapProvider, FilterListFragment.FilterListCloser {
    @Inject
    lateinit var mMapController: GoogleMapController


    internal var mBinding: RadarFragmentBinding? = null

    private var mSnackbar: Snackbar? = null


    private var mBindingReadyListener: BindingReadyListener? = null


    override fun onCreate(inSavedInstanceState: Bundle?) {
        super.onCreate(inSavedInstanceState)

        mMapController!!.SetProvider(this)
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

        mBinding!!.playPauseButton.SetPlayPauseState(mMapController!!.GetPlayPauseState())
        mBinding!!.playPauseButton.setOnClickListener { mMapController!!.TogglePlay() }

        mBinding!!.transparencySeekBar.max = 255
        mBinding!!.transparencySeekBar.progress = Math.round(255.0f * (1 - mMapController!!.GetTransparency()))
        mBinding!!.transparencySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(inSeekBar: SeekBar, inProgress: Int, inFromUser: Boolean) {
                mMapController!!.SetTransparency(1 - inProgress / 255.0f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        mSnackbar!!.show()

        return mBinding!!.root
    }


    override fun onResume() {
        super.onResume()

        mBinding!!.map.onResume()
    }


    override fun onPause() {
        mBinding!!.map.onPause()

        super.onPause()
    }


    override fun onDestroy() {
        mMapController.SetProvider(null)
        mBinding!!.map.onDestroy()

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


    override fun GetMap(inListener: GoogleMapListener) {
        val bindingReadyListener = object : BindingReadyListener {
            override fun BindingReady(inBinding: RadarFragmentBinding?) {
                if (inListener != null && inBinding != null) {
                    inBinding.map.getMapAsync { inMap -> inListener.MapFound(getContext()!!, inMap) }
                }
            }
        }

        if (mBinding == null)
            mBindingReadyListener = bindingReadyListener
        else
            bindingReadyListener.BindingReady(mBinding)
    }


    override fun SetTime(inTimeString: String?) {
        if (mSnackbar != null) {
            var timeString = ""

            if (inTimeString != null && !inTimeString.isEmpty()) {
                timeString = "\n" + inTimeString
                mBinding!!.playPauseButton.show()
            } else
                mBinding!!.playPauseButton.hide()

            mSnackbar!!.setText(mMapController!!.GetRadarName() + timeString)
        }
    }

    override fun Close() {
        RevealView(mBinding!!.radarLayout)
        mSnackbar!!.show()
        mBinding!!.playPauseButton.show()
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


    private interface BindingReadyListener {
        fun BindingReady(inBinding: RadarFragmentBinding?)
    }
}
