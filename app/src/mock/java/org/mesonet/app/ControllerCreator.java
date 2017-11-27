package org.mesonet.app;


import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.MesonetModel;
import org.mesonet.app.mesonetdata.MesonetSiteDataController;
import org.mesonet.app.mesonetdata.MockMesonetDataController;
import org.mesonet.app.mesonetdata.MockMesonetSiteDataController;
import org.mesonet.app.mesonetdata.SiteSelectionInterfaces;
import org.mesonet.app.userdata.Preferences;



public class ControllerCreator
{
    private static ControllerCreator sControllerCreator;



    public static ControllerCreator GetInstance()
    {
        if(sControllerCreator == null)
            sControllerCreator = new ControllerCreator();

        return sControllerCreator;
    }



    public MesonetDataController GetMesonetDataController(String inStid, Preferences inPreferences, SiteSelectionInterfaces.SelectSiteController inSelectSiteController)
    {
        return new MockMesonetDataController(inPreferences, inSelectSiteController);
    }



    public MesonetSiteDataController GetMesonetSiteDataController()
    {
        return new MockMesonetSiteDataController();
    }
}
