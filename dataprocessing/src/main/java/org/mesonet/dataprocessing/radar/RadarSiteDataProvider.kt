package org.mesonet.dataprocessing.radar


import android.app.Activity
import android.location.Location
import android.util.Xml
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider
import org.mesonet.models.radar.RadarDetailCreator
import org.mesonet.models.radar.RadarDetails
import org.mesonet.models.radar.RadarModel

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.Observable

import javax.inject.Inject


@org.mesonet.core.PerFragment
class RadarSiteDataProvider @Inject
constructor(inContext: Activity, private var mThreadHandler: ThreadHandler, private var mRadarDetailCreator: RadarDetailCreator) : Observable(), FilterListDataProvider, SelectSiteListener {
    private var mSelectedRadar = "KTLX"
    private var mRadarDetail: Map<String, RadarDetails> = HashMap()




    init {
        mThreadHandler.Run("RadarData", Runnable {
            val radarList = inContext.resources.openRawResource(inContext.resources.getIdentifier("radar_list", "raw", inContext.packageName))
            mRadarDetail = ProcessRadarXml(radarList)
            setChanged()
            notifyObservers()
        })
    }


    private fun ProcessRadarXml(inRadarXmlStream: InputStream): Map<String, RadarDetails> {
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

        return result
    }



    internal fun GetRadarDetail(inKey: String = mSelectedRadar): RadarDetails {
        return mRadarDetail!![inKey]!!
    }


    internal fun GetRadarName(): String {
        return mSelectedRadar + " - " + mRadarDetail[mSelectedRadar]!!.GetName()
    }

    override fun AllViewHolderData(inListener: FilterListDataProvider.FilterListDataListener) {
        val data = LinkedHashMap<String, BasicListData>()

        mThreadHandler.Run("RadarData", Runnable {
            val keys = ArrayList(mRadarDetail.keys)

            for (i in keys.indices) {
                val radar = mRadarDetail[keys[i]]

                val location = Location("none")
                location.latitude = radar?.GetLatitude()!!.toDouble()
                location.longitude = radar.GetLongitude()!!.toDouble()

                data[keys[i]] = object: BasicListData{
                    override fun GetName(): String {
                        return keys[i] + " - " + mRadarDetail[keys[i]]!!.GetName()
                    }

                    override fun IsFavorite(): Boolean {
                        return false
                    }

                    override fun GetLocation(): Location {
                        return location
                    }
                }
            }
        }, Runnable {
            inListener.ListDataBuilt(data)
        })
    }

    override fun CurrentSelection(): String {
        return mSelectedRadar
    }

    override fun GetDataObservable(): Observable {
        return this
    }

    override fun SetResult(inResult: String) {
        mThreadHandler.Run("RadarData", Runnable {
            mSelectedRadar = inResult
            setChanged()
            notifyObservers()
        })
    }
}
