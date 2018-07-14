package org.mesonet.dataprocessing.radar

import android.app.Activity
import android.location.Location
import android.util.Xml
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
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
class RadarSiteDataProvider @Inject
constructor(inContext: Activity, var mRadarDetailCreator: RadarDetailCreator) : FilterListDataProvider, SelectSiteListener {
    private var mSelectedSiteNameSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private var mSelectedSiteDetailSubject: BehaviorSubject<RadarDetails> = BehaviorSubject.create()

    private var mSiteSubject: BehaviorSubject<Map<String, RadarDetails>> = BehaviorSubject.create()

    init {
        mSelectedSiteNameSubject.observeOn(Schedulers.computation()).subscribe {
            if(mSiteSubject.hasValue() && mSiteSubject.value.containsKey(it))
                mSelectedSiteDetailSubject.onNext(mSiteSubject.value[it]!!)
        }
        Observable.create(ObservableOnSubscribe<String> {
            it.onNext(inContext.resources.openRawResource(inContext.resources.getIdentifier("radar_list", "raw", inContext.packageName)).bufferedReader().use { it.readText() })
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).map {
            mRadarDetailCreator.ParseRadarJson(it)
        }.subscribe{
            mSiteSubject.onNext(it)
            var initRadar = "KTLX"

            if (!it.containsKey(initRadar))
                initRadar = it.keys.first()

            mSelectedSiteNameSubject.onNext(initRadar)
            mSelectedSiteDetailSubject.onNext(it[initRadar]!!)
        }
    }



    fun GetSelectedSiteNameSubject(): BehaviorSubject<String>
    {
        return mSelectedSiteNameSubject
    }


    fun GetSelectedSiteDetailSubject(): BehaviorSubject<RadarDetails>
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
                        return it.value.GetName()!!
                    }

                    override fun IsFavorite(): Boolean {
                        return false
                    }

                    override fun GetLocation(): Location {
                        val location = Location("none")

                        location.latitude = it.value.GetLatitude()!!.toDouble()
                        location.longitude = it.value.GetLongitude()!!.toDouble()

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