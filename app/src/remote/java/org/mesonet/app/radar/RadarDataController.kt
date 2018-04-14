package org.mesonet.app.radar


import org.mesonet.app.DataDownloader
import org.mesonet.app.dependencyinjection.PerFragment

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

import javax.inject.Inject


//@PerFragment
class RadarDataController @Inject
constructor(internal var mDataDownloader: DataDownloader, inSiteDataProvider: RadarSiteDataProvider) : BaseRadarDataController(inSiteDataProvider) {




    init {
        Update()
    }


    override fun Update() {
        mDataDownloader.Download(String.format("http://www.mesonet.org/data/nids/maps/realtime/frames_%s_N0Q.xml", mSiteDataProvider.CurrentSelection()), 0, object : DataDownloader.DownloadCallback {
            override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                ProcessRadarXml(ByteArrayInputStream(inResult?.toByteArray(StandardCharsets.UTF_8)))
            }


            override fun DownloadFailed() {

            }
        }, null)
    }
}
