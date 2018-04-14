package org.mesonet.app.site.mesonetdata


import org.mesonet.app.DataDownloader
import org.mesonet.app.androidsystem.DeviceLocation
import org.mesonet.app.dependencyinjection.PerActivity
import org.mesonet.app.site.caching.SiteCache

import java.net.HttpURLConnection

import javax.inject.Inject


@PerActivity
class MesonetSiteDataController @Inject
constructor(inDeviceLocation: DeviceLocation, inCache: SiteCache, internal var mDataDownloader: DataDownloader) : BaseMesonetSiteDataController(inDeviceLocation, inCache) {


    init {
        LoadData()
    }


    override fun LoadData() {
        mCache.GetSites(object : SiteCache.SitesCacheListener {
            override fun SitesLoaded(inResults: MesonetSiteListModel) {
                if (mMesonetSiteModel == null || mMesonetSiteModel!!.size == 0)
                    SetData(inResults)
            }
        })
        mDataDownloader.Download("http://www.mesonet.org/find/siteinfo-json", 0, object : DataDownloader.DownloadCallback {
            override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                if (inResponseCode == HttpURLConnection.HTTP_OK)
                    SetData(inResult)
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
