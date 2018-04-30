package org.mesonet.network


import android.os.Handler
import android.util.Log
import org.mesonet.core.ThreadHandler

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

import javax.inject.Inject
import kotlin.collections.HashMap


class DataDownloader @Inject
internal constructor(internal var mThreadHandler: ThreadHandler)
{
    private val mIdsToDownloadInfo = HashMap<UUID, DownloadInfo>()

    fun StartDownloads(inUrl: String, inDownloadCallback: DownloadCallback, inUpdateInterval: Long = -1): UUID
    {
        val uuid = UUID.randomUUID()
        val downloadInfo = DownloadInfo(inUrl, inDownloadCallback, inUpdateInterval)

        synchronized(this@DataDownloader)
        {
            mIdsToDownloadInfo.put(uuid, downloadInfo)
        }

        var downloadResponseInfo: ResponseInfo? = null

        mThreadHandler.Run("Download", Runnable {
            if(mIdsToDownloadInfo[uuid] != null)
                downloadResponseInfo = Download(uuid, mIdsToDownloadInfo[uuid]!!, inDownloadCallback)
        }, Runnable {
            if(downloadResponseInfo != null)
            {
                if(downloadResponseInfo?.mFailed == null || downloadResponseInfo?.mFailed!!)
                    inDownloadCallback.DownloadFailed()
                else if(downloadResponseInfo?.mResponse != null && downloadResponseInfo?.mResponseCode != null)
                    inDownloadCallback.DownloadComplete(downloadResponseInfo?.mResponseCode!!, downloadResponseInfo?.mResponse!!)
            }
        })

        return uuid
    }


    fun StopDownloads(inTaskId: UUID)
    {
        synchronized(this@DataDownloader)
        {
            if (mIdsToDownloadInfo.containsKey(inTaskId))
                mIdsToDownloadInfo.remove(inTaskId)
        }
    }



    fun ForceUpdate(inTaskId: UUID)
    {
        if(mIdsToDownloadInfo.containsKey(inTaskId)) {
            val downloadInfo = mIdsToDownloadInfo[inTaskId]
            StopDownloads(inTaskId)
            if(downloadInfo != null)
                StartDownloads(downloadInfo.mUrl, downloadInfo.mDownloadCallback, downloadInfo.mUpdateInterval)
        }
    }



    internal fun Download(inTaskId: UUID, inDownloadInfo: DownloadInfo, inDownloadCallback: DownloadCallback): ResponseInfo
    {
        Log.e("Downloading", inDownloadInfo.mUrl)
        var failed = false
        var responseCode = -1
        var result = ""

        try {
            val url = URL(inDownloadInfo.mUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 10000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            conn.doInput = true

            conn.ifModifiedSince = inDownloadInfo.mLastModified

            conn.connect()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val resultString = StringBuilder()

                val scanner = Scanner(conn.inputStream)

                while (scanner.hasNextLine()) {
                    resultString.append(scanner.nextLine())
                }

                scanner.close()

                conn.disconnect()

                result = resultString.toString()
                responseCode = conn.responseCode
            }
        } catch (e: IOException) {
            e.printStackTrace()
            failed = true
        }

        if(mIdsToDownloadInfo.containsKey(inTaskId) && inDownloadInfo.mUpdateInterval >= 0)
        {
            Handler().postDelayed({
                Download(inTaskId, inDownloadInfo, inDownloadCallback)
            }, inDownloadInfo.mUpdateInterval)
        }

        Log.e("Download complete", inDownloadInfo.mUrl)
        return ResponseInfo(result, responseCode, failed)
    }

    interface DownloadCallback {
        fun DownloadComplete(inResponseCode: Int, inResult: String?)
        fun DownloadFailed()
    }



    internal class DownloadInfo(internal var mUrl: String, internal var mDownloadCallback: DownloadCallback, internal var mUpdateInterval: Long) {
        internal var mLastModified: Long = -1
    }


    internal class ResponseInfo(internal var mResponse: String? = null, internal var mResponseCode: Int = 0, internal var mFailed: Boolean = false)
}
