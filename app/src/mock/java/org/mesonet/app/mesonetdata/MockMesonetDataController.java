package org.mesonet.app.mesonetdata;


import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.userdata.Preferences;



public class MockMesonetDataController extends MesonetDataController
{
    private MockMesonetDataController (MesonetModel inMesonetModel,
                                      Preferences inPreferences)
    {
        super(inMesonetModel,
              inPreferences);
    }



    public static MesonetDataController NewInstance(String inMesonetString, Preferences inPreferences)
    {
        return new MesonetDataController(MesonetModel.NewInstance(inMesonetString), inPreferences);
    }
}
