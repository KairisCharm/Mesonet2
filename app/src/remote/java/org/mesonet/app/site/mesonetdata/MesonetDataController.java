package org.mesonet.app.site.mesonetdata;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import org.mesonet.app.DataDownloader;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.userdata.Preferences;

import java.net.HttpURLConnection;

import javax.inject.Inject;


@PerFragment
public class MesonetDataController extends BaseMesonetDataController
{
    @Inject
    public MesonetDataController (MesonetSiteDataController inSiteDataController, Preferences inPreferences)
    {
        super(inSiteDataController, inPreferences);
    }



    @Override
    public void StartUpdating()
    {
        super.StartUpdating();
        Update(60000);
    }



    private void Update (long inInterval)
    {

        boolean resumeUpdating = false;
        if(!mSiteDataController.CurrentSelection().equals(GetStid()) )
        {
            resumeUpdating = IsUpdating();
            StopUpdating();
        }

        final boolean finalResumeUpdating = resumeUpdating;

        DataDownloader.GetInstance()
                .Download("http://www.mesonet.org/index.php/app/latest_iphone/" + mSiteDataController.CurrentSelection(),
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
                                    if(finalResumeUpdating)
                                        StartUpdating();
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
                                Update(inLastUpdated);
                            }



                            @Override
                            public boolean ContinueUpdates ()
                            {
                                return IsUpdating();
                            }



                            @Override
                            public long GetInterval ()
                            {
                                return 60000;
                            }
                        });
    }
}
