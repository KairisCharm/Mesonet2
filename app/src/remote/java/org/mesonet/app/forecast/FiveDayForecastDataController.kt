package org.mesonet.app.forecast


import org.mesonet.app.DataDownloader
import org.mesonet.app.site.mesonetdata.MesonetSiteDataController

import java.util.Observable
import java.util.Observer

import javax.inject.Inject

class FiveDayForecastDataController @Inject
constructor(internal var mMesonetSiteDataController: MesonetSiteDataController, internal var mDataDownloader: DataDownloader) : BaseFiveDayForecastDataController(), Observer {


    init {
        Update()
    }


    private fun Update() {
        if (!mMesonetSiteDataController.SiteDataFound()) {
            mMesonetSiteDataController.addObserver(this)
            return
        }
        mDataDownloader.Download("http://www.mesonet.org/index.php/app/forecast/" + mMesonetSiteDataController.CurrentSelection(), 0, object : DataDownloader.DownloadCallback {
            override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
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
                return 60000
            }
        })
    }

    override fun update(o: Observable, arg: Any?) {
        Update()
    }
}
