package org.mesonet.models.radar

import org.xmlpull.v1.XmlPullParser

interface RadarImageCreator
{
    fun ParseRadarImagesXml(inXmlParser: XmlPullParser, inLimit: Int): List<RadarImageModel>
}