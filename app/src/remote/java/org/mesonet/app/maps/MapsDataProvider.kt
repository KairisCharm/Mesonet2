package org.mesonet.app.maps


import com.google.gson.Gson

import org.mesonet.app.DataDownloader

import java.net.HttpURLConnection

import javax.inject.Inject

class MapsDataProvider @Inject
constructor() : BaseMapsDataProvider() {
    @Inject
    lateinit var mDataDownloader: DataDownloader


    override fun GetMaps(inListListener: BaseMapsDataProvider.MapsListListener) {
        mDataDownloader.Download("http://content.mesonet.org/mesonet/mobile-app/products.json", 0, object : DataDownloader.DownloadCallback {
            override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode <= HttpURLConnection.HTTP_PARTIAL) {
                    inListListener.ListLoaded(Gson().fromJson(inResult, MapsModel::class.java))
                }
            }

            override fun DownloadFailed() {

            }
        }, object : DataDownloader.DownloadUpdateCheckListener {
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
