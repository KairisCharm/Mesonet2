package org.mesonet.dataprocessing.advisories

import org.mesonet.core.ThreadHandler
import org.mesonet.network.DataDownloader
import java.net.HttpURLConnection
import java.util.*

import javax.inject.Inject

class AdvisoryDataProvider @Inject constructor(private var mThreadHandler: ThreadHandler) : Observable() {

    @Inject
    internal lateinit var mAdvisoryParser: AdvisoryParser

    internal var mDataDownloader: DataDownloader

    private var mCurrentAdvisories: ArrayList<AdvisoryModel>? = null


    init{
        mDataDownloader = DataDownloader(mThreadHandler)
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


    fun StartUpdates() {
        if(!mDataDownloader.IsUpdating()) {
            mThreadHandler.Run("AdvisoryData", Runnable {
                mDataDownloader.StartDownloads("https://www.mesonet.org/data/public/noaa/wwa/mobile-ok.txt", object : DataDownloader.DownloadCallback {
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
    }



    fun StopUpdates()
    {
        mDataDownloader.StopDownloads()
    }
}
