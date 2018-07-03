package org.mesonet.app.radar

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.dataprocessing.radar.GoogleMapController



class PlayPauseButton(mContext: Context, inAttrs: AttributeSet) : FloatingActionButton(mContext, inAttrs), Observer<Boolean> {
    private var mPlayPauseState: GoogleMapController.PlayPauseState? = null

    internal fun SetPlayPauseState(inPlayPauseState: GoogleMapController.PlayPauseState) {
        mPlayPauseState = inPlayPauseState
        mPlayPauseState?.observeOn(AndroidSchedulers.mainThread())?.subscribe(this)
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}

    override fun onNext(t: Boolean) {
        if(t)
            setImageResource(R.drawable.ic_pause_white_36dp)
        else
            setImageResource(R.drawable.ic_play_arrow_white_36dp)
    }
}