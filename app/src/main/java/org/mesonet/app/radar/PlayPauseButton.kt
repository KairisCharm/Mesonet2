package org.mesonet.app.radar


import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet

import org.mesonet.app.R

import java.util.Observable
import java.util.Observer


class PlayPauseButton(inContext: Context, inAttrs: AttributeSet) : FloatingActionButton(inContext, inAttrs), Observer {
    private var mPlayPauseState: GoogleMapController.PlayPauseState? = null


    fun SetPlayPauseState(inPlayPauseState: GoogleMapController.PlayPauseState) {
        mPlayPauseState = inPlayPauseState
        mPlayPauseState!!.addObserver(this)
    }


    override fun update(o: Observable, arg: Any?) {
        if (mPlayPauseState!!.GetIsPlaying())
            setImageResource(R.drawable.ic_pause_white_36dp)
        else
            setImageResource(R.drawable.ic_play_arrow_white_36dp)
    }
}