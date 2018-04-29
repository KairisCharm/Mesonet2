package org.mesonet.app.radar


import android.app.Activity
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet

import org.mesonet.app.R
import org.mesonet.dataprocessing.radar.GoogleMapController

import java.util.Observable
import java.util.Observer


class PlayPauseButton(private var mContext: Context, inAttrs: AttributeSet) : FloatingActionButton(mContext, inAttrs), Observer {
    private var mPlayPauseState: GoogleMapController.PlayPauseState? = null


    internal fun SetPlayPauseState(inPlayPauseState: GoogleMapController.PlayPauseState) {
        mPlayPauseState = inPlayPauseState
        mPlayPauseState!!.addObserver(this)
    }


    override fun update(o: Observable, arg: Any?) {
        if(mContext is Activity) {
            (mContext as Activity).runOnUiThread({
                if (mPlayPauseState!!.GetIsPlaying())
                    setImageResource(R.drawable.ic_pause_white_36dp)
                else
                    setImageResource(R.drawable.ic_play_arrow_white_36dp)
            })
        }
    }
}