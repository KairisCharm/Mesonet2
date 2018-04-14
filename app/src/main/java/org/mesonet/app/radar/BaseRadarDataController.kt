package org.mesonet.app.radar


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Xml

import com.squareup.picasso.Picasso

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


abstract class BaseRadarDataController(internal var mSiteDataProvider: RadarSiteDataProvider) : Observable(), Observer {

    internal lateinit var mRadarImages: List<RadarImageModel>


    init {

        mSiteDataProvider.addObserver(this)
    }


    protected fun ProcessRadarXml(inRadarXmlStream: InputStream) {
        val xmlParser = Xml.newPullParser()

        try {
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlParser.setInput(inRadarXmlStream, null)
            mRadarImages = ParseRadarXml(xmlParser, 6)
            setChanged()
            notifyObservers()
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

    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun ParseRadarXml(inXmlParser: XmlPullParser, inLimit: Int): List<RadarImageModel> {
        val result = ArrayList<RadarImageModel>()

        while (inXmlParser.next() != XmlPullParser.END_DOCUMENT && result.size < inLimit) {
            if (inXmlParser.eventType != XmlPullParser.START_TAG)
                continue

            val name = inXmlParser.name
            // Starts by looking for the entry tag
            if (name == "frame") {
                val builder = RadarImageModel.Builder(this)
                for (i in 0 until inXmlParser.attributeCount) {
                    builder.SetValue(inXmlParser.getAttributeName(i), inXmlParser.getAttributeValue(i))
                }

                result.add(builder.Build())
            }
        }

        return result
    }


    fun GetRadarImageDetails(): List<RadarImageModel>? {
        return mRadarImages
    }


    fun GetImage(inContext: Context, inImagePath: String, inImageLoadedListener: ImageLoadedListener) {
        var callingLooper = Looper.myLooper()

        if (callingLooper == null) {
            Looper.prepare()
            callingLooper = Looper.myLooper()
        }

        val finalCallingLooper = callingLooper

        Thread(Runnable {
            try {
                val original = Picasso.with(inContext).load(inImagePath).get()

                val matrix = Matrix()
                matrix.postScale(2f, 2f)
                val resizedBitmap = Bitmap.createBitmap(1200, 1200, Bitmap.Config.ARGB_8888)

                val canvas = Canvas(resizedBitmap)
                canvas.drawBitmap(original, matrix, Paint())
                canvas.save()

                original.recycle()

                Handler(finalCallingLooper, Handler.Callback {
                    inImageLoadedListener.BitmapLoaded(resizedBitmap)
                    false
                }).sendEmptyMessage(0)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }


    override fun update(o: Observable, arg: Any?) {
        Update()
    }


    protected abstract fun Update()

    interface ImageLoadedListener {
        fun BitmapLoaded(inResult: Bitmap)
    }
}
