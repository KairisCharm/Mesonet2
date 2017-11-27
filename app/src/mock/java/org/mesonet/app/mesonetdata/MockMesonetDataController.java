package org.mesonet.app.mesonetdata;

import android.os.Handler;

import org.mesonet.app.userdata.Preferences;



public class MockMesonetDataController extends MesonetDataController
{
    private static final String[] dataSamples = {
                                    // mildly chilly day
                                     "STNM=121," +
                                     "TIME=1509549600," +
                                     "RELH=76," +
                                     "TAIR=12.6," +
                                     "WSPD=6.8," +
                                     "WVEC=6.6," +
                                     "WDIR=181," +
                                     "WDSD=12.8," +
                                     "WSSD=1.5," +
                                     "WMAX=11.3," +
                                     "RAIN=0.00," +
                                     "PRES=964.79," +
                                     "SRAD=402," +
                                     "TA9M=12.1," +
                                     "WS2M=5.9," +
                                     "TS10=12.2," +
                                     "TB10=9.6," +
                                     "TS05=12.3," +
                                     "TS25=14.4," +
                                     "TS60=16.9," +
                                     "TR05=1.6," +
                                     "TR25=1.5," +
                                     "TR60=1.5," +
                                     "RAIN_24H=0.00," +
                                     "RAIN_24=0.00," +
                                     "STID=NRMN," +
                                     "YEAR=2017," +
                                     "MONTH=11," +
                                     "DAY=01," +
                                     "HOUR=15," +
                                     "MINUTE=20," +
                                     "SECOND=0",

                                    // hot day
                                      "STNM=15," +
                                     "TIME=1343809245," +
                                     "RELH=12," +
                                     "TAIR=45.0," +
                                     "WSPD=3.0," +
                                     "WVEC=2.6," +
                                     "WDIR=322," +
                                     "WDSD=28.1," +
                                     "WSSD=1.4," +
                                     "WMAX=5.5," +
                                     "RAIN=0.00," +
                                     "PRES=979.58," +
                                     "SRAD=868," +
                                     "TA9M=43.4," +
                                     "WS2M=2.2," +
                                     "TS10=-995," +
                                     "TB10=-995," +
                                     "TS05=-995," +
                                     "TS25=-995," +
                                     "TS60=-995," +
                                     "TR05=-995," +
                                     "TR25=-995," +
                                     "TR60=-995," +
                                     "RAIN_24H=1.23," +
                                     "RAIN_24=1.23," +
                                     "STID=BRIS," +
                                     "YEAR=2012," +
                                     "MONTH=8," +
                                     "DAY=1," +
                                     "HOUR=3," +
                                     "MINUTE=20," +
                                     "SECOND=45"};



    private int mSampleIndex = 0;


    public MockMesonetDataController (Preferences inPreferences,
                                      SiteSelectionInterfaces.SelectSiteController inSelectSiteController)
    {
        super(null,
              inPreferences,
                inSelectSiteController);
    }



    @Override
    public void StartUpdating()
    {
        if(!IsUpdating())
        {
            super.StartUpdating();

            new Handler().post(new Runnable()
            {
                @Override
                public void run ()
                {
                    mSampleIndex++;

                    if (mSampleIndex >= dataSamples.length) mSampleIndex = 0;

                    SetData(dataSamples[mSampleIndex]);

                    if (IsUpdating())
                    {
                        new Handler().postDelayed(this,
                                                  10000);
                    }
                }
            });
        }
    }
}
