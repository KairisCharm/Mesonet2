package org.mesonet.dataprocessing.advisories

import org.mesonet.core.ThreadHandler
import org.mesonet.network.DataDownloader
import java.net.HttpURLConnection
import java.util.*

import javax.inject.Inject

class AdvisoryDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader, internal var mThreadHandler: ThreadHandler) : Observable() {

    @Inject
    internal lateinit var mAdvisoryParser: AdvisoryParser

    private var mCurrentAdvisories: ArrayList<AdvisoryModel>? = null
    private var mTaskId: UUID? = null


    init{
        mThreadHandler.Run("AdvisoryData", Runnable {
            StartUpdates()
        })
    }


    internal fun SetData(inData: String) {
        mCurrentAdvisories = mAdvisoryParser.ParseAdvisoryFile(inData)
        setChanged()
        notifyObservers()
    }


    fun GetAdvisories(): ArrayList<AdvisoryModel>? {
        return mCurrentAdvisories
    }


    fun GetCount(): Int {
        return if (mCurrentAdvisories == null) 0 else mCurrentAdvisories!!.size
    }


    internal fun StartUpdates() {
        mThreadHandler.Run("AdvisoryData", Runnable {
            mTaskId = mDataDownloader.StartDownloads("https://www.mesonet.org/data/public/noaa/wwa/mobile-ok.txt", object : DataDownloader.DownloadCallback {
                override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                    if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                        SetData(inResult!!)
                    }
                }

                override fun DownloadFailed() {

                }
            }, 60000)
        })
    }



    fun StopUpdates()
    {
        if(mTaskId != null)
            mDataDownloader.StopDownloads(mTaskId!!)
    }
}
