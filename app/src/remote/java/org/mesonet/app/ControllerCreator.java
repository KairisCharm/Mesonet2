package org.mesonet.app;

import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.RemoteMesonetDataController;
import org.mesonet.app.userdata.Preferences;



public class ControllerCreator
{
    private static ControllerCreator sControllerCreator;

    private RemoteMesonetDataController mMesonetDataController;



    public static ControllerCreator GetInstance()
    {
        if(sControllerCreator == null)
            sControllerCreator = new ControllerCreator();

        return sControllerCreator;
    }




    public MesonetDataController GetMesonetDataController (String inStid, Preferences inPreferences)
    {
        if(mMesonetDataController == null)
            mMesonetDataController = new RemoteMesonetDataController(null, inPreferences);

        mMesonetDataController.SetStid(inStid);

        return mMesonetDataController;
    }
}
