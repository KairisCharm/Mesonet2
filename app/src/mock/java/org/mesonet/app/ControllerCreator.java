package org.mesonet.app;


import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.MesonetModel;
import org.mesonet.app.userdata.Preferences;



public class ControllerCreator
{
    static final String kGoodTestString = "STNM=121," +
                                          "TIME=1509238500," +
                                          "RELH=56," +
                                          "TAIR=4.0," +
                                          "WSPD=1.8," +
                                          "WVEC=1.8," +
                                          "WDIR=161," +
                                          "WDSD=4.9," +
                                          "WSSD=0.2," +
                                          "WMAX=2.3," +
                                          "RAIN=0.13," +
                                          "PRES=975.97," +
                                          "SRAD=0.5," +
                                          "TA9M=8.8," +
                                          "WS2M=0.9," +
                                          "TS10=14.2," +
                                          "TB10=12.1," +
                                          "TS05=13.7," +
                                          "TS25=15.6," +
                                          "TS60=18.5," +
                                          "TR05=1.6," +
                                          "TR25=1.5," +
                                          "TR60=1.5," +
                                          "RAIN_24H=1.23," +
                                          "RAIN_24=1.23," +
                                          "STID=NRMN," +
                                          "YEAR=2017," +
                                          "MONTH=10," +
                                          "DAY=29," +
                                          "HOUR=13," +
                                          "MINUTE=55," +
                                          "SECOND=45";

    private static ControllerCreator sControllerCreator;



    public static ControllerCreator GetInstance()
    {
        if(sControllerCreator == null)
            sControllerCreator = new ControllerCreator();

        return sControllerCreator;
    }



    public MesonetDataController GetMesonetDataController(String inStid, Preferences inPreferences)
    {
        return new MesonetDataController(MesonetModel.NewInstance(kGoodTestString), inPreferences);
    }
}
