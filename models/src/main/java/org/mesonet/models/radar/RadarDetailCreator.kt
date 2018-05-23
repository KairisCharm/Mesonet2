package org.mesonet.models.radar

import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

interface RadarDetailCreator
{
    fun ParseRadarXml(inXmlParser: XmlPullParser): Map<String, RadarDetails>
}