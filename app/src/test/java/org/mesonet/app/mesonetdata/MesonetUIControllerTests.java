package org.mesonet.app.mesonetdata;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mesonet.app.mesonetdata.MesonetModelTests.kGoodTestString;



public class MesonetUIControllerTests
{
    @Test
    public void NullControllerStringsTests()
    {
        MesonetUIController nullControllerController = new MesonetUIController(null);

        assertEquals("_", nullControllerController.GetAirTempString());
        assertEquals("_", nullControllerController.GetApparentTempString());
        assertEquals("_", nullControllerController.GetDewPointString());
        assertEquals("_", nullControllerController.GetWindString());
        assertEquals("_", nullControllerController.Get24HrRainfallString());
    }



    @Test
    public void NullDataStringsTests()
    {
        MesonetUIController nullDataController = new MesonetUIController(new MesonetDataController(null, null));

        assertEquals("_", nullDataController.GetAirTempString());
        assertEquals("_", nullDataController.GetApparentTempString());
        assertEquals("_", nullDataController.GetDewPointString());
        assertEquals("_", nullDataController.GetWindString());
        assertEquals("_", nullDataController.Get24HrRainfallString());
    }



    @Test
    public void EmptyDataStringsTests()
    {
        MesonetUIController nullDataController = new MesonetUIController(new MesonetDataController(new MesonetModel(), null));

        assertEquals("_", nullDataController.GetAirTempString());
        assertEquals("_", nullDataController.GetApparentTempString());
        assertEquals("_", nullDataController.GetDewPointString());
        assertEquals("_", nullDataController.GetWindString());
        assertEquals("_", nullDataController.Get24HrRainfallString());
    }



    @Test
    public void NoPreferenceStringsTests()
    {
        MesonetUIController nullDataController = new MesonetUIController(new MesonetDataController(MesonetModel.NewInstance(kGoodTestString), null));

        assertEquals("4°", nullDataController.GetAirTempString());
        assertEquals("_", nullDataController.GetApparentTempString());
        assertEquals("_", nullDataController.GetDewPointString());
        assertEquals("_", nullDataController.GetWindString());
        assertEquals("_", nullDataController.Get24HrRainfallString());
    }
}
