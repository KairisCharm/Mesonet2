package org.mesonet.models.radar

import android.util.Xml
import okhttp3.ResponseBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.util.ArrayList

class RadarImageModelConverterFactory: Converter.Factory()
{
    companion object {
        val kRadarImageLimit = 6
    }


    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, List<RadarImageInfo>>
    {
        return RadarImageModelConverter()
    }


    class RadarImageModelConverter: Converter<ResponseBody, List<RadarImageInfo>> {
        @Throws(XmlPullParserException::class, IOException::class)
        override fun convert(inValue: ResponseBody?): List<RadarImageInfo> {
            val result = ArrayList<RadarImageModel>()

            if (inValue != null) {
                val parser = Xml.newPullParser()
                try {
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                    parser.setInput(inValue.byteStream(), null)

                    while (parser.next() != XmlPullParser.END_DOCUMENT && result.size < kRadarImageLimit) {
                        if (parser.eventType != XmlPullParser.START_TAG)
                            continue

                        val name = parser.name
                        // Starts by looking for the entry tag
                        if (name == "frame") {
                            val builder = RadarImageModel.Builder()

                            for (i in 0 until parser.attributeCount) {
                                try {
                                    builder.SetValue(parser.getAttributeName(i), parser.getAttributeValue(i))
                                } catch (e: XmlPullParserException) {
                                    e.printStackTrace()
                                }
                            }

                            result.add(builder.Build())
                        }
                    }
                } catch (e: XmlPullParserException) {
                    e.printStackTrace()
                }
            }

            return result
        }
    }
}