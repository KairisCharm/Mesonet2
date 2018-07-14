package org.mesonet.app.radar

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

import org.mesonet.app.R
import org.mesonet.dataprocessing.radar.MapboxMapController



class PlayPauseButton(mContext: Context, inAttrs: AttributeSet) : FloatingActionButton(mContext, inAttrs), Observer<Boolean> {

    internal fun SetPlayPauseState(inPlayPauseState: Observable<Boolean>) {
        inPlayPauseState.observeOn(AndroidSchedulers.mainThread())?.subscribe(this)
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