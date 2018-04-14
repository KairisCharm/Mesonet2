package org.mesonet.app


import android.os.AsyncTask
import android.os.Handler
import android.os.Looper

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

import javax.inject.Inject


class DataDownloader @Inject
internal constructor() {


    fun Download(inUrl: String, inIfModifiedSince: Long, inDownloadCallback: DownloadCallback, inDownloadUpdateCheckListener: DownloadUpdateCheckListener?) {
        DownloadAsyncTask().execute(DownloadInfo(inUrl, inIfModifiedSince, inDownloadCallback, inDownloadUpdateCheckListener))
    }


    private class DownloadAsyncTask : AsyncTask<DownloadInfo, Any, Array<out DownloadInfo>>() {
        override fun doInBackground(vararg inDownloadInfo: DownloadInfo): Array<out DownloadInfo> {
            for (dlInfo in inDownloadInfo) {
                try {
                    val url = URL(dlInfo.mUrl)
                    val conn = url.openConnection() as HttpURLConnection
                    conn.readTimeout = 10000
                    conn.connectTimeout = 15000
                    conn.requestMethod = "GET"
                    conn.doInput = true

                    conn.ifModifiedSince = dlInfo.mModifiedSinceTime

                    conn.connect()

                    if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                        val resultString = StringBuilder()

                        val scanner = Scanner(conn.inputStream)

                        while (scanner.hasNextLine()) {
                            resultString.append(scanner.nextLine())
                        }

                        scanner.close()

                        conn.disconnect()

                        dlInfo.mResult = resultString.toString()
                        dlInfo.mResponseCode = conn.responseCode

                        if (dlInfo.mDownloadUpdateCheckListener != null && dlInfo.mDownloadUpdateCheckListener.ContinueUpdates()) {
                            SetUpdateTimer(conn.date, dlInfo.mDownloadUpdateCheckListener)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    dlInfo.mDownloadCallback.DownloadFailed()
                }

            }

            return inDownloadInfo
        }


        override fun onPostExecute(inResult: Array<out DownloadInfo>) {
            for (dlInfo in inResult) {
                dlInfo?.mDownloadCallback?.DownloadComplete(dlInfo.mResponseCode,
                        dlInfo.mResult)
            }
        }


        fun SetUpdateTimer(inLastUpdated: Long, inDownloadUpdateCheckListener: DownloadUpdateCheckListener?) {
            if (inDownloadUpdateCheckListener == null)
                return

            val interval = inDownloadUpdateCheckListener.GetInterval()

            if (interval > 0) {
                Handler(Looper.getMainLooper()).postDelayed({ inDownloadUpdateCheckListener.CheckForUpdate(inLastUpdated) },
                        interval)
            }
        }
    }


    interface DownloadCallback {
        fun DownloadComplete(inResponseCode: Int, inResult: String?)
        fun DownloadFailed()
    }


    interface DownloadUpdateCheckListener {
        fun CheckForUpdate(inLastUpdated: Long)
        fun ContinueUpdates(): Boolean
        fun GetInterval(): Long
    }


    private class DownloadInfo(internal val mUrl: String, internal val mModifiedSinceTime: Long, internal val mDownloadCallback: DownloadCallback, internal val mDownloadUpdateCheckListener: DownloadUpdateCheckListener?) {
        internal var mResult: String? = null
        internal var mResponseCode = -1
    }
}
