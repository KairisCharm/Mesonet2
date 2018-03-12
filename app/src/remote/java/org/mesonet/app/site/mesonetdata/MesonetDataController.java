package org.mesonet.app.site.mesonetdata;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import org.mesonet.app.DataDownloader;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.userdata.Preferences;

import java.net.HttpURLConnection;
import java.util.Observer;

import javax.inject.Inject;


@PerFragment
public class MesonetDataController extends BaseMesonetDataController implements Observer
{
    @Inject
    DataDownloader mDataDownloader;

    private long mLastUpdated = 0;



    @Inject
    public MesonetDataController (MesonetSiteDataController inSiteDataController, Preferences inPreferences)
    {
        super(inSiteDataController, inPreferences);
    }



    @Override
    public void StartUpdating()
    {
        if(!IsUpdating()) {
            super.StartUpdating();
            Update(60000);
        }
    }



    protected void Update (final long inInterval)
    {
        if(!mSiteDataController.SiteDataFound()) {
            StopUpdating();
            return;
        }

        mDataDownloader.Download("http://www.mesonet.org/index.php/app/latest_iphone/" + mSiteDataController.CurrentSelection(),
                        inInterval,
                        new DataDownloader.DownloadCallback()
                        {
                            @Override
                            public void DownloadComplete (int inResponseCode,
                                                          String inResult)
                            {
                                if (inResponseCode >= HttpURLConnection.HTTP_OK &&
                                        inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE)
                                {
                                    SetData(inResult);
                                }
                            }



                            @Override
                            public void DownloadFailed ()
                            {

                            }
                        },
                        new DataDownloader.DownloadUpdateCheckListener(){
                            @Override
                            public void CheckForUpdate (long inLastUpdated)
                            {
                                if(inLastUpdated > mLastUpdated) {
                                    mLastUpdated = inLastUpdated;
                                    Update(inInterval);
                                }
                            }



                            @Override
                            public boolean ContinueUpdates ()
                            {
                                return IsUpdating();
                            }



                            @Override
                            public long GetInterval ()
                            {
                                return inInterval;
                            }
                        });
    }
}
