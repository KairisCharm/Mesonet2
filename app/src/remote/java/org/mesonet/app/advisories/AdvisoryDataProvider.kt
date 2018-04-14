package org.mesonet.app.advisories


import org.mesonet.app.DataDownloader

import java.net.HttpURLConnection

import javax.inject.Inject

class AdvisoryDataProvider @Inject
constructor(var mDataDownloader: DataDownloader) : BaseAdvisoryDataProvider() {


    init {
        Update()
    }


    override fun Update() {
        mDataDownloader.Download("https://www.mesonet.org/data/public/noaa/wwa/mobile.txt", 60000, object : DataDownloader.DownloadCallback {
            override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                    SetData(inResult!!)
                }
            }

            override fun DownloadFailed() {

            }
        },
                object : DataDownloader.DownloadUpdateCheckListener {
                    override fun CheckForUpdate(inLastUpdated: Long) {

                    }

                    override fun ContinueUpdates(): Boolean {
                        return false
                    }

                    override fun GetInterval(): Long {
                        return 0
                    }
                })
    }
}
