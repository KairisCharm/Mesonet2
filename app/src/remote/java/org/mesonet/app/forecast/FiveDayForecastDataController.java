package org.mesonet.app.forecast;


import org.mesonet.app.DataDownloader;
import org.mesonet.app.site.mesonetdata.MesonetSiteDataController;

import javax.inject.Inject;

public class FiveDayForecastDataController extends BaseFiveDayForecastDataController
{
    @Inject
    public FiveDayForecastDataController(MesonetSiteDataController inSiteDataController)
    {
        DataDownloader.GetInstance().Download("http://www.mesonet.org/index.php/app/forecast/" + inSiteDataController.CurrentSelection(), 0, new DataDownloader.DownloadCallback() {
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
                return 0;
            }
        });
    }
}
