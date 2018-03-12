package org.mesonet.app.forecast;


import org.mesonet.app.DataDownloader;
import org.mesonet.app.site.mesonetdata.MesonetSiteDataController;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

public class FiveDayForecastDataController extends BaseFiveDayForecastDataController implements Observer
{
    DataDownloader mDataDownloader;

    MesonetSiteDataController mMesonetSiteDataController;


    @Inject
    public FiveDayForecastDataController(MesonetSiteDataController inSiteDataController, DataDownloader inDownloader)
    {
        mDataDownloader = inDownloader;
        mMesonetSiteDataController = inSiteDataController;
        Update();
    }



    private void Update()
    {
        if(!mMesonetSiteDataController.SiteDataFound())
        {
            mMesonetSiteDataController.addObserver(this);
            return;
        }
        mDataDownloader.Download("http://www.mesonet.org/index.php/app/forecast/" + mMesonetSiteDataController.CurrentSelection(), 0, new DataDownloader.DownloadCallback() {
            @Override
            public void DownloadComplete(int inResponseCode, String inResult) {
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
                return 60000;
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Update();
    }
}
