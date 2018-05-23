package org.mesonet.models.radar

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.HashMap
import javax.inject.Inject



class RadarDetailModelCreator @Inject constructor() : RadarDetailCreator
{
    @Throws(XmlPullParserException::class, IOException::class)
    override fun ParseRadarXml(inXmlParser: XmlPullParser): Map<String, RadarDetails> {
        val result = HashMap<String, RadarDetails>()

        try {
            while (inXmlParser.next() != XmlPullParser.END_DOCUMENT) {
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
        }
        catch (e: XmlPullParserException)
        {
            e.printStackTrace()
        }

        return result
    }


    @Throws(XmlPullParserException::class, IOException::class)
    internal fun ParseRadarDetail(inXmlParser: XmlPullParser): RadarDetails {
        val builder = RadarModel.Builder()

        try {
            var next = inXmlParser.next()
            while (next != XmlPullParser.END_DOCUMENT && (next != XmlPullParser.END_TAG || inXmlParser.name == null || inXmlParser.name != "dict")) {
                if (inXmlParser.eventType != XmlPullParser.START_TAG) {
                    next = inXmlParser.next()
                    continue
                }

                if (inXmlParser.name == "key") {
                    inXmlParser.next()

                    val key = inXmlParser.text

                    inXmlParser.next()
                    inXmlParser.next()
                    inXmlParser.next()

                    val type = inXmlParser.name

                    inXmlParser.next()

                    val value = inXmlParser.text

                    if(value != null)
                        builder.SetValue(key, type, value)
                }

                next = inXmlParser.next()
            }
        }
        catch (e: XmlPullParserException)
        {
            e.printStackTrace()
        }

        return builder.Build()
    }
}