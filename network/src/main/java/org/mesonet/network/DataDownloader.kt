package org.mesonet.network


import android.os.Handler
import android.util.Log
import org.mesonet.core.ThreadHandler

import java.io.IOException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_MULT_CHOICE
import java.net.URL
import java.util.*


class DataDownloader constructor(private var mThreadHandler: ThreadHandler)
{
    private var mIsUpdating = false
    private var mUpdateInterval: Long = -1
    private val mHandler = Handler()

    fun StartDownloads(inUrl: String, inDownloadCallback: DownloadCallback, inUpdateInterval: Long = -1)
    {
        mUpdateInterval = inUpdateInterval
        mIsUpdating = true

        SingleUpdate(inUrl, inDownloadCallback)
    }


    fun SingleUpdate(inUrl: String, inDownloadCallback: DownloadCallback)
    {
        val downloadInfo = DownloadInfo(inUrl, inDownloadCallback, mUpdateInterval)

        var downloadResponseInfo: ResponseInfo? = null

        mThreadHandler.Run("Download", Runnable {
            downloadResponseInfo = Download(downloadInfo, inDownloadCallback)
        }, Runnable {
            if(downloadResponseInfo != null)
            {
                if(downloadResponseInfo?.mFailed == null || downloadResponseInfo?.mFailed!!)
                    inDownloadCallback.DownloadFailed()
                else if(downloadResponseInfo?.mResponse != null && downloadResponseInfo?.mResponseCode != null)
                    inDownloadCallback.DownloadComplete(downloadResponseInfo?.mResponseCode!!, downloadResponseInfo?.mResponse!!)
            }
        })
    }


    fun StopDownloads()
    {
        mIsUpdating = false
    }


    internal fun Download(inDownloadInfo: DownloadInfo, inDownloadCallback: DownloadCallback): ResponseInfo
    {
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

            if (conn.responseCode >= HttpURLConnection.HTTP_OK && conn.responseCode < HTTP_MULT_CHOICE) {
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

        if(mIsUpdating && inDownloadInfo.mUpdateInterval > -1)
        {
            mHandler.postDelayed({
                mThreadHandler.Run("DataDownloader", Runnable {
                    if(mIsUpdating)
                        Download(inDownloadInfo, inDownloadCallback)
                })
            }, inDownloadInfo.mUpdateInterval)
        }

        if(failed)
            inDownloadCallback.DownloadFailed()
        else
            inDownloadCallback.DownloadComplete(responseCode, result)

        return ResponseInfo(result, responseCode, failed)
    }


    fun IsUpdating(): Boolean
    {
        return mIsUpdating
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
