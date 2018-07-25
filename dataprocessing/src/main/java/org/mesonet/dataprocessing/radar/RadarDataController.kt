package org.mesonet.dataprocessing.radar

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerFragment
import org.mesonet.models.radar.RadarDetails
import org.mesonet.models.radar.RadarHistory
import org.mesonet.network.DataProvider
import java.util.concurrent.TimeUnit

import javax.inject.Inject


@PerFragment
class RadarDataController @Inject
constructor(internal var mSiteDataProvider: RadarSiteDataProvider,
            internal var mDataProvider: DataProvider) : RadarImageDataProvider {
    private val mAnimation: ArrayList<ImageSubject> = ArrayList()


    private var mUpdateDisposable: Disposable? = null
    private val mRadarImageDataSubject: BehaviorSubject<List<ImageSubject>> = BehaviorSubject.create()
    private var mRadarImageDataObservable: Observable<List<ImageSubject>>
    private var mSelectedRadarDisposable: Disposable? = null


    init {
        mRadarImageDataObservable = mRadarImageDataSubject.doOnSubscribe {
            if (mUpdateDisposable == null || mUpdateDisposable?.isDisposed != true) {

                Observable.interval(0, 1, TimeUnit.MINUTES).subscribeOn(Schedulers.computation()).subscribe(object: Observer<Long>{
                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {
                        mUpdateDisposable = d
                    }

                    override fun onNext(t: Long) {
                        if (mRadarImageDataSubject.hasObservers()) {
                            GetSiteNameSubject().observeOn(Schedulers.computation()).subscribe(object : Observer<String> {
                                override fun onComplete() {}
                                override fun onSubscribe(d: Disposable) {
                                    mSelectedRadarDisposable = d
                                }

                                override fun onNext(radarId: String) {
                                    mDataProvider.GetRadarHistory(radarId).observeOn(Schedulers.computation()).subscribe(object : Observer<RadarHistory> {
                                        override fun onComplete() {}
                                        override fun onSubscribe(d: Disposable) {}

                                        override fun onNext(t: RadarHistory) {
                                            val relevantList = t.GetFrames().subList(0, 6)

                                            val newIds = relevantList.map { frame -> MakeId(t.GetRadar(), frame) }.toTypedArray()
                                            val oldIds = mAnimation.map { it.GetName() }.toTypedArray()

                                            if (!newIds.contentEquals(oldIds)) {
                                                mAnimation.filter { !newIds.contains(it.GetName()) }.forEach {
                                                    if (it.GetSubject().hasValue())
                                                        it.GetSubject().value.recycle()
                                                }

                                                val oldValuesBuf: ArrayList<ImageSubject> = ArrayList()

                                                var countHandled = 0

                                                for (i in relevantList.indices) {
                                                    if (i >= mAnimation.size) {
                                                        mAnimation.add(ImageSubjectImpl())
                                                    }

                                                    if (i >= oldIds.size || newIds[i] != oldIds[i]) {
                                                        if (i < oldIds.size && newIds.toList().subList(i + 1, newIds.size).contains(oldIds[i]))
                                                            oldValuesBuf.add(mAnimation[i])
                                                        else if (mAnimation[i].GetSubject().hasValue())
                                                            mAnimation[i].GetSubject().value.recycle()

                                                        var oldValue = mAnimation.subList(i, mAnimation.size).find { it.GetName() == newIds[i] }

                                                        if (oldValue == null)
                                                            oldValue = oldValuesBuf.find { it.GetName() == newIds[i] }

                                                        if (oldValue != null) {
                                                            (mAnimation[i] as ImageSubjectImpl).mName = oldValue.GetName()
                                                            (mAnimation[i] as ImageSubjectImpl).mSubject.onNext(oldValue.GetSubject().value)

                                                            continue
                                                        }
                                                    }

                                                    (mAnimation[i] as ImageSubjectImpl).mName = newIds[i]

                                                    mDataProvider.GetRadarImage(radarId, relevantList[i].GetFrameId()).observeOn(Schedulers.computation()).subscribe(object : Observer<Bitmap> {
                                                        override fun onComplete() {
                                                            if (countHandled == relevantList.size)
                                                                mRadarImageDataSubject.onNext(mAnimation)
                                                        }

                                                        override fun onSubscribe(d: Disposable) {}

                                                        override fun onError(e: Throwable) {
                                                            e.printStackTrace()
                                                        }

                                                        override fun onNext(t: Bitmap) {
                                                            (mAnimation[i] as ImageSubjectImpl).mName = newIds[i]
                                                            (mAnimation[i] as ImageSubjectImpl).mTimestring = relevantList[i].GetTimestring()
                                                            (mAnimation[i] as ImageSubjectImpl).mSubject.onNext(t)

                                                            countHandled++
                                                        }
                                                    })
                                                }
                                            }
                                        }

                                        override fun onError(e: Throwable) {
                                            e.printStackTrace()
                                        }
                                    })
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    onNext("KTLX")
                                }

                            })
                        } else
                            mUpdateDisposable?.dispose()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
            }
        }
    }


    override fun GetSiteNameSubject(): BehaviorSubject<String> {
        return mSiteDataProvider.GetSelectedSiteNameSubject()
    }


    override fun GetSiteDetailSubject(): BehaviorSubject<RadarDetails> {
        return mSiteDataProvider.GetSelectedSiteDetailSubject()
    }


    override fun GetRadarAnimationObservable(): Observable<List<ImageSubject>> {
        return mRadarImageDataObservable
    }


    private fun MakeId(inRadarId: String, inFrame: RadarHistory.RadarFrame): String {
        return inRadarId + inFrame.GetTime()
    }


    override fun Dispose()
    {
        mSiteDataProvider.Dispose()
        mUpdateDisposable?.dispose()
        mSelectedRadarDisposable?.dispose()
        mRadarImageDataSubject.onComplete()

        for(frame in mAnimation)
            frame.GetSubject().onComplete()
    }


    inner class ImageSubjectImpl(var mName: String = "",
                                 var mTimestring: String = "",
                                 var mSubject: BehaviorSubject<Bitmap> = BehaviorSubject.create()) : ImageSubject
    {
        override fun GetName(): String {
            return mName
        }

        override fun GetTimestring(): String {
            return mTimestring
        }

        override fun GetSubject(): BehaviorSubject<Bitmap> {
            return mSubject
        }
    }


    interface ImageSubject
    {
        fun GetName(): String
        fun GetTimestring(): String
        fun GetSubject(): BehaviorSubject<Bitmap>
    }
}