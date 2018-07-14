package org.mesonet.dataprocessing.radar

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerFragment
import java.util.concurrent.TimeUnit

import javax.inject.Inject



@PerFragment
class MapboxMapController @Inject constructor()
{
    private var mPlayPauseSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private var mSelectedImageIndexSubject: BehaviorSubject<Int> = BehaviorSubject.create()

    private var mPlayPauseDisposable: Disposable? = null

    private var mFrameCount = 0

    private var mTransparencySubject: BehaviorSubject<Float> = BehaviorSubject.create()

    init {
        mTransparencySubject.onNext(0.7f)
        mSelectedImageIndexSubject.onNext(0)
        mPlayPauseSubject.onNext(false)

        mPlayPauseSubject.observeOn(Schedulers.computation()).subscribe {
            mPlayPauseDisposable?.dispose()

            if(it)
            {
                mPlayPauseDisposable = Observable.interval(0, 1, TimeUnit.SECONDS).observeOn(Schedulers.computation()).subscribe {
                    synchronized(mSelectedImageIndexSubject)
                    {
                        var selectIndex = 0

                        if (mSelectedImageIndexSubject.hasValue())
                            selectIndex = mSelectedImageIndexSubject.value

                        selectIndex--

                        if (selectIndex < 0) {
                            selectIndex = mFrameCount - 1
                        }

                        if (selectIndex >= mFrameCount)
                            selectIndex = 0

                        mSelectedImageIndexSubject.onNext(selectIndex)
                    }
                }
            }
            else
            {
                mSelectedImageIndexSubject.onNext(0)
            }
        }
    }


    fun GetCameraPositionObservable(inLat: Double, inLon: Double, inZoom: Double): Observable<CameraPosition>
    {
        return Observable.create (ObservableOnSubscribe<CameraPosition>{
            it.onNext(CameraPosition.Builder()
                    .target(LatLng(inLat, inLon))
                    .zoom(inZoom)
                    .build())
        }).subscribeOn(Schedulers.computation())
    }



    fun SetFrameCount(inFrameCount: Int)
    {
        mFrameCount = inFrameCount
    }



    fun GetActiveImageIndexObservable(): Observable<Int>
    {
        return mSelectedImageIndexSubject
    }


    fun GetPlayPauseStateObservable(): Observable<Boolean>
    {
        return mPlayPauseSubject
    }


    fun TogglePlay()
    {
        mPlayPauseSubject.onNext(!mPlayPauseSubject.value)
    }



    fun GetTransparencySubject(): BehaviorSubject<Float>
    {
        return mTransparencySubject
    }
}