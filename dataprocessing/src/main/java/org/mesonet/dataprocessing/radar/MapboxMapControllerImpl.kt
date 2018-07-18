package org.mesonet.dataprocessing.radar

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerFragment
import java.util.concurrent.TimeUnit

import javax.inject.Inject



@PerFragment
class MapboxMapControllerImpl @Inject constructor(): MapboxMapController
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

        mPlayPauseSubject.observeOn(Schedulers.computation()).subscribe(object: Observer<Boolean>
        {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Boolean) {
                mPlayPauseDisposable?.dispose()

                if(t)
                {
                    Observable.interval(0, 1, TimeUnit.SECONDS).observeOn(Schedulers.computation()).subscribe (object: Observer<Long>{
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                            mPlayPauseDisposable = d
                        }

                        override fun onNext(t: Long) {
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

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            onNext(false)
                        }

                    })
                }
                else
                {
                    mSelectedImageIndexSubject.onNext(0)
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                onNext(false)
            }

        })
    }


    override fun GetCameraPositionObservable(inLat: Double, inLon: Double, inZoom: Double): Observable<CameraPosition>
    {
        return Observable.create (ObservableOnSubscribe<CameraPosition>{
            it.onNext(CameraPosition.Builder()
                    .target(LatLng(inLat, inLon))
                    .zoom(inZoom)
                    .build())
        }).subscribeOn(Schedulers.computation())
    }



    override fun SetFrameCount(inFrameCount: Int)
    {
        mFrameCount = inFrameCount
    }



    override fun GetActiveImageIndexObservable(): Observable<Int>
    {
        return mSelectedImageIndexSubject
    }


    override fun GetPlayPauseStateObservable(): Observable<Boolean>
    {
        return mPlayPauseSubject
    }


    override fun TogglePlay()
    {
        mPlayPauseSubject.onNext(!mPlayPauseSubject.value)
    }



    override fun GetTransparencySubject(): BehaviorSubject<Float>
    {
        return mTransparencySubject
    }
}