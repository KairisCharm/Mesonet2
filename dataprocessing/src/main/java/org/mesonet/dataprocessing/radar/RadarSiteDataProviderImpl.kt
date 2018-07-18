package org.mesonet.dataprocessing.radar

import android.app.Activity
import android.location.Location
import android.util.Xml
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerActivity
import org.mesonet.core.PerFragment
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider
import org.mesonet.models.radar.RadarDetails
import org.mesonet.models.radar.RadarDetailCreator

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.util.HashMap

import javax.inject.Inject
import java.io.*


@PerFragment
class RadarSiteDataProviderImpl @Inject
constructor(inContext: Activity, var mRadarDetailCreator: RadarDetailCreator) : RadarSiteDataProvider {
    private var mSelectedSiteNameSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private var mSelectedSiteDetailSubject: BehaviorSubject<RadarDetails> = BehaviorSubject.create()

    private var mSiteSubject: BehaviorSubject<Map<String, RadarDetails>> = BehaviorSubject.create()

    init {
        mSelectedSiteNameSubject.observeOn(Schedulers.computation()).subscribe(object: Observer<String>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: String) {
                if(mSiteSubject.hasValue() && mSiteSubject.value.containsKey(t))
                    mSelectedSiteDetailSubject.onNext(mSiteSubject.value[t]?: object: RadarDetails{
                        override fun GetLatitude(): Float? {
                            return null
                        }

                        override fun GetLongitude(): Float? {
                            return null
                        }

                        override fun GetSouthWestCorner(): RadarDetails.Corner? {
                            return null
                        }

                        override fun GetNorthEastCorner(): RadarDetails.Corner? {
                            return null
                        }

                        override fun GetName(): String? {
                            return null
                        }
                    })
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                onNext("KTLX")
            }

        })

        Observable.create(ObservableOnSubscribe<String> {
            it.onNext(inContext.resources.openRawResource(inContext.resources.getIdentifier("radar_list", "raw", inContext.packageName)).bufferedReader().use { it.readText() })
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).map {
            mRadarDetailCreator.ParseRadarJson(it)
        }.subscribe(object: Observer<Map<String, RadarDetails>> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: Map<String, RadarDetails>) {
                mSiteSubject.onNext(t)
                var initRadar = "KTLX"

                if (!t.containsKey(initRadar))
                    initRadar = t.keys.first()

                mSelectedSiteNameSubject.onNext(initRadar)
                mSelectedSiteDetailSubject.onNext(t[initRadar]?: t.values.first())
            }
        })
    }



    override fun GetSelectedSiteNameSubject(): BehaviorSubject<String>
    {
        return mSelectedSiteNameSubject
    }


    override fun GetSelectedSiteDetailSubject(): BehaviorSubject<RadarDetails>
    {
        return mSelectedSiteDetailSubject
    }


    fun GetDataSubject(): BehaviorSubject<Map<String, RadarDetails>> {
        return mSiteSubject
    }


    override fun CurrentSelection(): String {
        return mSelectedSiteNameSubject.value
    }


    override fun AsBasicListData(): Observable<Pair<Map<String, BasicListData>, String>>
    {
        return Observable.create (
            ObservableOnSubscribe<Pair<Map<String, BasicListData>, String>> {
            val resultMap = mSiteSubject.value.mapValues {
                object : BasicListData {
                    override fun GetName(): String {
                        return it.value.GetName()?: ""
                    }

                    override fun IsFavorite(): Boolean {
                        return false
                    }

                    override fun GetLocation(): Location {
                        val location = Location("none")

                        location.latitude = it.value.GetLatitude()?.toDouble()?: 0.0
                        location.longitude = it.value.GetLongitude()?.toDouble()?: 0.0

                        return location
                    }

                }
            }

            it.onNext(Pair(resultMap, mSelectedSiteNameSubject.value))
        }).subscribeOn(Schedulers.computation())
    }


    override fun SetResult(inResult: String) {
        mSelectedSiteNameSubject.onNext(inResult)
    }
}