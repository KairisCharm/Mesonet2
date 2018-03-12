package org.mesonet.app.maps;


import com.google.gson.Gson;

import org.mesonet.app.DataDownloader;

import java.net.HttpURLConnection;

import javax.inject.Inject;

public class MapsDataProvider extends BaseMapsDataProvider
{
    @Inject
    DataDownloader mDataDownloader;

    @Inject
    public MapsDataProvider(){}



    @Override
    public void GetMaps(final MapsListListener inListListener)
    {
        mDataDownloader.Download("http://content.mesonet.org/mesonet/mobile-app/products.json", 0, new DataDownloader.DownloadCallback() {
            @Override
            public void DownloadComplete(int inResponseCode, String inResult) {
                if(inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode <= HttpURLConnection.HTTP_PARTIAL)
                {
                    inListListener.ListLoaded(new Gson().fromJson(inResult, MapsModel.class));
                }
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
