package org.mesonet.app.mesonetdata;


import org.junit.Test;
import org.mesonet.app.formulas.UnitConverter;

import static junit.framework.Assert.assertEquals;
import static org.mesonet.app.mesonetdata.MesonetModelTests.kGoodTestString;

public class MesonetDataControllerTests
{
    @Test
    public void MesonetNullDataTests()
    {
        MesonetDataController nullDataController = new MesonetDataController(null, null);

        assertEquals(null, nullDataController.GetTemp());
        assertEquals(null, nullDataController.GetApparentTemp());
        assertEquals(null, nullDataController.GetDewPoint());
        assertEquals(null, nullDataController.GetWindDirection());
        assertEquals(null, nullDataController.GetWindSpd());
        assertEquals(null, nullDataController.Get24HrRain());
        assertEquals(null, nullDataController.GetHumidity());
        assertEquals(null, nullDataController.GetMaxWind());
        assertEquals(null, nullDataController.GetPressure());
    }



    @Test
    public void MesonetEmptyDataTests()
    {
        MesonetDataController nullDataController = new MesonetDataController(MesonetModel.NewInstance(""), null);

        assertEquals(0.0, nullDataController.GetTemp());
        assertEquals(0.0, nullDataController.GetApparentTemp());
        assertEquals(0.0, nullDataController.GetDewPoint());
        assertEquals(UnitConverter.CompassDirections.N, nullDataController.GetWindDirection());
        assertEquals(0.0, nullDataController.GetWindSpd());
        assertEquals(0.0, nullDataController.Get24HrRain());
        assertEquals(0, (int)nullDataController.GetHumidity());
        assertEquals(0.0, nullDataController.GetMaxWind());
        assertEquals(0.0, nullDataController.GetPressure());
    }



    @Test
    public void MesonetGoodDataNoPreferenceTests()
    {
        MesonetDataController nullDataController = new MesonetDataController(MesonetModel.NewInstance(kGoodTestString), null);

        assertEquals(4.0, nullDataController.GetTemp());
        assertEquals(4.0, nullDataController.GetApparentTemp());
        assertEquals(0.0, nullDataController.GetDewPoint());
        assertEquals(UnitConverter.CompassDirections.SSE, nullDataController.GetWindDirection());
        assertEquals(1.8, nullDataController.GetWindSpd());
        assertEquals(1.23, nullDataController.Get24HrRain());
        assertEquals(56, (int)nullDataController.GetHumidity());
        assertEquals(2.3, nullDataController.GetMaxWind());
        assertEquals(975.97, nullDataController.GetPressure());
    }
}
