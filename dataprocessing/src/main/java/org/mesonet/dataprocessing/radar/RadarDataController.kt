package org.mesonet.dataprocessing.radar


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Xml
import com.squareup.picasso.Picasso
import org.mesonet.core.ThreadHandler
import org.mesonet.network.DataDownloader
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

import javax.inject.Inject


class RadarDataController @Inject
constructor(private var mSiteDataProvider: RadarSiteDataProvider, private var mThreadHandler: ThreadHandler) : Observable(), Observer {

    companion object {
        val kRadarImageLimit = 6
    }

    private var mRadarImages: List<RadarImageModel>? = null
    private var mCurrentRadar = ""

    private var mDataDownloader: DataDownloader


    init {
        mDataDownloader = DataDownloader(mThreadHandler)
        mThreadHandler.Run("RadarData", Runnable {
            mSiteDataProvider.addObserver(this)
        })
    }

    fun StartUpdates()
    {
        if(!mDataDownloader.IsUpdating()) {
            mThreadHandler.Run("RadarData", Runnable {
                mCurrentRadar = mSiteDataProvider.CurrentSelection()
                mDataDownloader.StartDownloads(String.format("http://www.mesonet.org/data/nids/maps/realtime/frames_%s_N0Q.xml", mSiteDataProvider.CurrentSelection()), object : DataDownloader.DownloadCallback {
                    override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                        ProcessRadarXml(ByteArrayInputStream(inResult?.toByteArray(StandardCharsets.UTF_8)))
                    }


                    override fun DownloadFailed() {

                    }
                }, 60000)
            })
        }
    }


    fun StopUpdates()
    {
        mThreadHandler.Run("RadarData", Runnable {
            mDataDownloader.StopDownloads()
        })
    }



    protected fun ProcessRadarXml(inRadarXmlStream: InputStream) {
        val xmlParser = Xml.newPullParser()

        try {
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlParser.setInput(inRadarXmlStream, null)
            mRadarImages = ParseRadarXml(xmlParser, kRadarImageLimit)
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


    internal fun GetRadarImageDetails(): List<RadarImageModel>? {
        return mRadarImages
    }


    internal fun GetImage(inContext: Context, inImagePath: String, inImageLoadedListener: ImageLoadedListener) {
        try {
            val original = Picasso.with(inContext).load(inImagePath).get()

            val matrix = Matrix()
            matrix.postScale(2f, 2f)
            val resizedBitmap = Bitmap.createBitmap(1200, 1200, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(resizedBitmap)
            canvas.drawBitmap(original, matrix, Paint())
            canvas.save()

            original.recycle()

            inImageLoadedListener.BitmapLoaded(resizedBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    override fun update(o: Observable, arg: Any?) {
        mThreadHandler.Run("RadarData", Runnable {
            if(mCurrentRadar.equals(mSiteDataProvider.CurrentSelection())) {
                StopUpdates()
                while (mDataDownloader.IsUpdating());
                StartUpdates()
            }
        })
    }



    internal interface ImageLoadedListener {
        fun BitmapLoaded(inResult: Bitmap)
    }
}
