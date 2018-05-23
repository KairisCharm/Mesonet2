package org.mesonet.dataprocessing.advisories

import org.mesonet.core.ThreadHandler
import org.mesonet.models.advisories.Advisory
import org.mesonet.models.advisories.AdvisoryCreator
import org.mesonet.models.advisories.AdvisoryModel
import org.mesonet.network.DataDownloader
import java.net.HttpURLConnection
import java.util.*

import javax.inject.Inject

class AdvisoryDataProvider @Inject constructor(private var mThreadHandler: ThreadHandler) : Observable() {

    @Inject
    internal lateinit var mAdvisoryCreator: AdvisoryCreator

    internal var mDataDownloader: DataDownloader

    private var mCurrentAdvisories: ArrayList<Advisory>? = null


    init{
        mDataDownloader = DataDownloader(mThreadHandler)
    }


    internal fun SetData(inData: String) {
        mCurrentAdvisories = mAdvisoryCreator.ParseAdvisoryFile(inData)
        setChanged()
        notifyObservers()
    }


    fun GetAdvisories(): ArrayList<Advisory>? {
        return mCurrentAdvisories
    }

    override fun addObserver(o: Observer?) {
        super.addObserver(o)
        setChanged()
        notifyObservers()
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
