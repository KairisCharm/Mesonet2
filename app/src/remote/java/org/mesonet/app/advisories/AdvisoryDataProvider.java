package org.mesonet.app.advisories;


import org.mesonet.app.DataDownloader;

import java.net.HttpURLConnection;

import javax.inject.Inject;

public class AdvisoryDataProvider extends BaseAdvisoryDataProvider
{
    DataDownloader mDataDownloader;


    @Inject
    public AdvisoryDataProvider(DataDownloader inDownloader)
    {
        mDataDownloader = inDownloader;
        Update();
    }


    @Override
    protected void Update() {
        mDataDownloader.Download("https://www.mesonet.org/data/public/noaa/wwa/mobile.txt", 60000, new DataDownloader.DownloadCallback() {
                    @Override
                    public void DownloadComplete(int inResponseCode, String inResult) {
                        if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                            SetData(inResult);
                        }
                    }

                    @Override
                    public void DownloadFailed() {

                    }
                },
                new DataDownloader.DownloadUpdateCheckListener() {
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
