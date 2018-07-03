package org.mesonet.dataprocessing.radar

import android.app.Activity
import android.location.Location
import android.util.Xml
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
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


@org.mesonet.core.PerFragment
class RadarSiteDataProvider @Inject
constructor(inContext: Activity, var mRadarDetailCreator: RadarDetailCreator) : Observable<Pair<Map<String, RadarDetails>, String>>(), FilterListDataProvider, SelectSiteListener {

    private var mSelectedRadar = "KTLX"
    private var mRadarDetail: Map<String, RadarDetails> = HashMap()

    init {
        Observable.create(ObservableOnSubscribe<String> {
            it.onNext(inContext.resources.openRawResource(inContext.resources.getIdentifier("radar_list", "raw", inContext.packageName)).bufferedReader().use { it.readText() })
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe{
                ProcessRadarXml(it.byteInputStream()).observeOn(Schedulers.computation()).subscribe {
                    mRadarDetail = it
                }
        }
    }


    private fun ProcessRadarXml(inRadarXmlStream: InputStream): Observable<Map<String, RadarDetails>> {
        return Observable.create (ObservableOnSubscribe<Map<String, RadarDetails>>{
            val xmlParser = Xml.newPullParser()

            var result: Map<String, RadarDetails> = HashMap()

            try {
                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                xmlParser.setInput(inRadarXmlStream, null)
                result = mRadarDetailCreator.ParseRadarXml(xmlParser)
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                inRadarXmlStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            it.onNext(result)
        }).subscribeOn(Schedulers.computation())
    }



    internal fun GetRadarDetail(inKey: String = mSelectedRadar): RadarDetails {
        return mRadarDetail[inKey]!!
    }


    internal fun GetRadarName(): String {
        return mSelectedRadar + " - " + mRadarDetail[mSelectedRadar]!!.GetName()
    }


    override fun CurrentSelection(): String {
        return mSelectedRadar
    }


    override fun subscribeActual(observer: Observer<in Pair<Map<String, RadarDetails>, String>>?) {
        observer?.onNext(Pair(mRadarDetail, mSelectedRadar))
    }


    override fun AsBasicListData(): Observable<Pair<Map<String, BasicListData>, String>>
    {
        return Observable.create {observer ->
            this.observeOn(Schedulers.computation()).map {
                val resultMap = it.first.mapValues {
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

                Pair(resultMap, mSelectedRadar)
            }.subscribe{
                observer.onNext(it)
            }
        }
    }


    override fun SetResult(inResult: String) {
            mSelectedRadar = inResult
    }
}