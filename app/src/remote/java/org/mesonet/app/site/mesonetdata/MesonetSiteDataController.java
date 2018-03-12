package org.mesonet.app.site.mesonetdata;


import org.mesonet.app.DataDownloader;
import org.mesonet.app.androidsystem.DeviceLocation;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.site.caching.SiteCache;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Singleton;


@PerActivity
public class MesonetSiteDataController extends BaseMesonetSiteDataController
{
    DataDownloader mDataDownloader;


    @Inject
    public MesonetSiteDataController(DeviceLocation inDeviceLocation, SiteCache inCache, DataDownloader inDataDownloader)
    {
        super(inDeviceLocation, inCache);
        mDataDownloader = inDataDownloader;
        LoadData();
    }



    @Override
    protected void LoadData()
    {
        mCache.GetSites(new SiteCache.SitesCacheListener() {
            @Override
            public void SitesLodaded(MesonetSiteListModel inResults) {
                if(mMesonetSiteModel == null || mMesonetSiteModel.size() == 0)
                    SetData(inResults);
            }
        });
        mDataDownloader.Download("http://www.mesonet.org/find/siteinfo-json", 0, new DataDownloader.DownloadCallback() {
            @Override
            public void DownloadComplete(int inResponseCode, String inResult) {
                if(inResponseCode == HttpURLConnection.HTTP_OK)
                    SetData(inResult);
            }

            @Override
            public void DownloadFailed() {

            }
        }, new DataDownloader.DownloadUpdateCheckListener() {
            @Override
            public void CheckForUpdate(long inLastUpdated) {

            }

            @Override
            public boolean ContinueUpdates() {
                return false;
            }

            @Override
            public long GetInterval() {
                return 0;
            }
        });
    }
}
