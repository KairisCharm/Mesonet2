package org.mesonet.app.radar;


import org.mesonet.app.DataDownloader;
import org.mesonet.app.dependencyinjection.PerFragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;



//@PerFragment
public class RadarDataController extends BaseRadarDataController
{
    DataDownloader mDataDownloader;




    @Inject
    public RadarDataController(DataDownloader inDataDownloader, RadarSiteDataProvider inSiteDataProvider)
    {
        super(inSiteDataProvider);
        mDataDownloader = inDataDownloader;
        Update();
    }



    @Override
    protected void Update()
    {
        mDataDownloader.Download(String.format("http://www.mesonet.org/data/nids/maps/realtime/frames_%s_N0Q.xml", mSiteDataProvider.CurrentSelection()), 0, new DataDownloader.DownloadCallback() {
            @Override
            public void DownloadComplete(int inResponseCode, String inResult)
            {
                ProcessRadarXml(new ByteArrayInputStream(inResult.getBytes(StandardCharsets.UTF_8)));
            }



            @Override
            public void DownloadFailed()
            {

            }
        }, null);
    }
}
