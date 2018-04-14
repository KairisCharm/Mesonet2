package org.mesonet.app.radar


import android.app.Activity
import android.location.Location
import android.util.Xml

import org.mesonet.app.BasicViewHolder
import org.mesonet.app.dependencyinjection.PerFragment
import org.mesonet.app.filterlist.FilterListFragment
import org.mesonet.app.site.SiteSelectionInterfaces
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.Observable

import javax.inject.Inject


@PerFragment
class RadarSiteDataProvider @Inject
constructor(inContext: Activity) : Observable(), FilterListFragment.FilterListDataProvider, SiteSelectionInterfaces.SelectSiteListener {
    private var mSelectedRadar = "KTLX"
    internal var mRadarDetail: Map<String, RadarModel.RadarDetailModel>? = null


    init {
        val radarList = inContext.resources.openRawResource(inContext.resources.getIdentifier("radar_list", "raw", inContext.packageName))
        mRadarDetail = ProcessRadarXml(radarList)
        setChanged()
        notifyObservers()
    }


    private fun ProcessRadarXml(inRadarXmlStream: InputStream): Map<String, RadarModel.RadarDetailModel>? {
        val xmlParser = Xml.newPullParser()

        var result: Map<String, RadarModel.RadarDetailModel>? = null

        try {
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlParser.setInput(inRadarXmlStream, null)
            result = ParseRadarXml(xmlParser)
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


    @Throws(XmlPullParserException::class, IOException::class)
    private fun ParseRadarXml(inXmlParser: XmlPullParser): Map<String, RadarModel.RadarDetailModel> {
        val result = HashMap<String, RadarModel.RadarDetailModel>()

        while (inXmlParser.next() != XmlPullParser.END_TAG) {
            if (inXmlParser.eventType != XmlPullParser.START_TAG)
                continue

            val name = inXmlParser.name
            // Starts by looking for the entry tag
            if (name == "key") {
                if (inXmlParser.next() == XmlPullParser.TEXT) {
                    val key = inXmlParser.text

                    inXmlParser.next()
                    inXmlParser.next()
                    inXmlParser.next()

                    if (inXmlParser.name == "dict")
                        result[key] = ParseRadarDetail(inXmlParser)
                }
            }
        }

        return result
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun ParseRadarDetail(inXmlParser: XmlPullParser): RadarModel.RadarDetailModel {
        val builder = RadarModel.RadarDetailModel.Builder()

        while (inXmlParser.next() != XmlPullParser.END_TAG || inXmlParser.name == null || inXmlParser.name != "dict") {
            if (inXmlParser.eventType != XmlPullParser.START_TAG)
                continue

            if (inXmlParser.name == "key") {
                inXmlParser.next()

                val key = inXmlParser.text

                inXmlParser.next()
                inXmlParser.next()
                inXmlParser.next()

                val type = inXmlParser.name

                inXmlParser.next()

                val value = inXmlParser.text

                builder.SetValue(key, type, value)
            }
        }

        return builder.Build()
    }


    @JvmOverloads
    fun GetRadarDetail(inKey: String = mSelectedRadar): RadarModel.RadarDetailModel {
        return mRadarDetail!![inKey]!!
    }


    fun GetRadarName(): String {
        return mSelectedRadar + " - " + mRadarDetail!![mSelectedRadar]!!.GetName()
    }

    override fun AllViewHolderData(): Map<String, BasicViewHolder.BasicViewHolderData> {
        val data = LinkedHashMap<String, BasicViewHolder.BasicViewHolderData>()

        val keys = ArrayList(mRadarDetail!!.keys)

        for (i in keys.indices) {
            val radar = mRadarDetail!![keys[i]]

            val location = Location("none")
            location.latitude = radar?.GetLatitude()!!.toDouble()
            location.longitude = radar.GetLongitude().toDouble()

            data[keys[i]] = BasicViewHolder.BasicViewHolderData(keys[i] + " - " + mRadarDetail!![keys[i]]!!.GetName(), location, false)
        }

        return data
    }

    override fun CurrentSelection(): String {
        return mSelectedRadar
    }

    override fun GetDataObservable(): Observable {
        return this
    }

    override fun SetResult(inResult: String) {
        mSelectedRadar = inResult
        setChanged()
        notifyObservers()
    }
}
