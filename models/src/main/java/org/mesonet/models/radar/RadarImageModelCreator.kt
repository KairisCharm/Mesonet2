package org.mesonet.models.radar

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.ArrayList
import javax.inject.Inject

class RadarImageModelCreator @Inject constructor() : RadarImageCreator {

    @Throws(XmlPullParserException::class, IOException::class)
    override fun ParseRadarImagesXml(inXmlParser: XmlPullParser, inLimit: Int): List<RadarImageModel> {
        val result = ArrayList<RadarImageModel>()

        try {

            while (inXmlParser.next() != XmlPullParser.END_DOCUMENT && result.size < inLimit) {
                if (inXmlParser.eventType != XmlPullParser.START_TAG)
                    continue

                val name = inXmlParser.name
                // Starts by looking for the entry tag
                if (name == "frame") {
                    val builder = RadarImageModel.Builder()

                    for (i in 0 until inXmlParser.attributeCount) {
                        try {
                            builder.SetValue(inXmlParser.getAttributeName(i), inXmlParser.getAttributeValue(i))
                        }
                        catch (e: XmlPullParserException)
                        {
                            e.printStackTrace()
                        }
                    }

                    result.add(builder.Build())
                }
            }
        }
        catch (e: XmlPullParserException)
        {
            e.printStackTrace()
        }

        return result
    }
}