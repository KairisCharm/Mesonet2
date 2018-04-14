package org.mesonet.app.site.mesonetdata

import org.mesonet.app.DataDownloader
import org.mesonet.app.dependencyinjection.PerFragment
import org.mesonet.app.userdata.Preferences

import java.net.HttpURLConnection
import java.util.Observer

import javax.inject.Inject


@PerFragment
class MesonetDataController @Inject
constructor(inSiteDataController: MesonetSiteDataController, inPreferences: Preferences) : BaseMesonetDataController(inSiteDataController, inPreferences), Observer {
    @Inject
    lateinit var mDataDownloader: DataDownloader

    private var mLastUpdated: Long = 0


    override fun StartUpdating() {
        if (!IsUpdating()) {
            super.StartUpdating()
            Update(60000)
        }
    }


    protected fun Update(inInterval: Long) {
        if (!mSiteDataController.SiteDataFound()) {
            StopUpdating()
            return
        }

        mDataDownloader.Download("http://www.mesonet.org/index.php/app/latest_iphone/" + mSiteDataController.CurrentSelection()!!,
                inInterval,
                object : DataDownloader.DownloadCallback {
                    override fun DownloadComplete(inResponseCode: Int,
                                                  inResult: String?) {
                        if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                            SetData(inResult!!)
                        }
                    }


                    override fun DownloadFailed() {

                    }
                },
                object : DataDownloader.DownloadUpdateCheckListener {
                    override fun CheckForUpdate(inLastUpdated: Long) {
                        if (inLastUpdated > mLastUpdated) {
                            mLastUpdated = inLastUpdated
                            Update(inInterval)
                        }
                    }


                    override fun ContinueUpdates(): Boolean {
                        return IsUpdating()
                    }


                    override fun GetInterval(): Long {
                        return inInterval
                    }
                })
    }
}
