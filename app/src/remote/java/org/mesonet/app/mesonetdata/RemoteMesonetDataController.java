package org.mesonet.app.mesonetdata;


import org.mesonet.app.DataDownloader;
import org.mesonet.app.userdata.Preferences;

import java.net.HttpURLConnection;



public class RemoteMesonetDataController extends MesonetDataController
{
    private String mDownloadingStid;



    public RemoteMesonetDataController (MesonetModel inMesonetModel,
                                        Preferences inPreferences)
    {
        super(inMesonetModel,
              inPreferences);
    }



    public void SetStid (String inStid)
    {
        if(inStid == null)

        StopUpdating();
        SetStid(inStid, 0);
    }



    @Override
    public void StartUpdating()
    {
        super.StartUpdating();
        SetStid(GetStid() == null ? mDownloadingStid : GetStid());
    }



    private void SetStid (String inStid, long inInterval)
    {
        if(inStid == null)
            return;

        mDownloadingStid = inStid;

        boolean resumeUpdating = false;
        if(!inStid.equals(GetStid()) )
        {
            resumeUpdating = IsUpdating();
            StopUpdating();
        }

        final boolean finalResumeUpdating = resumeUpdating;

        DataDownloader.GetInstance()
                      .Download("http://www.mesonet.org/index.php/app/latest_iphone/" + inStid,
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
                                        SetStid(GetStid(), inLastUpdated);
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
