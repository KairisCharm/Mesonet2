package org.mesonet.app.mesonetdata;


import org.junit.Test;
import org.mesonet.app.formulas.MathMethods;
import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.userdata.Preferences;

import java.util.Observable;

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
    public void MesonetGoodDataNoPreferenceTests()
    {
        MesonetDataController nullDataController = new MesonetDataController(MesonetModel.NewInstance(kGoodTestString), null);

        assertEquals(4.0, nullDataController.GetTemp());
        assertEquals(2.42748, MathMethods.GetInstance().Round(nullDataController.GetApparentTemp(), 5));
        assertEquals(-3.96622, MathMethods.GetInstance().Round(nullDataController.GetDewPoint(), 5));
        assertEquals(UnitConverter.CompassDirections.SSE, nullDataController.GetWindDirection());
        assertEquals(1.8, nullDataController.GetWindSpd());
        assertEquals(1.23, nullDataController.Get24HrRain());
        assertEquals(56, (int)nullDataController.GetHumidity());
        assertEquals(2.3, nullDataController.GetMaxWind());
        assertEquals(975.97, nullDataController.GetPressure());
    }



    @Test
    public void MesonetGoodDataMetricTests()
    {
        MesonetDataController nullDataController = new MesonetDataController(MesonetModel.NewInstance(kGoodTestString),
                                                                             new Preferences(){
                                                                                 @Override
                                                                                 public UnitPreference GetUnitPreference ()
                                                                                 {
                                                                                     return UnitPreference.kMetric;
                                                                                 }

                                                                                 @Override
                                                                                 public Observable GetObservable() {
                                                                                     return null;
                                                                                 }
                                                                             });

        assertEquals(4.0, nullDataController.GetTemp());
        assertEquals(2.42748, MathMethods.GetInstance().Round(nullDataController.GetApparentTemp(), 5));
        assertEquals(-3.96622, MathMethods.GetInstance().Round(nullDataController.GetDewPoint(), 5));
        assertEquals(UnitConverter.CompassDirections.SSE, nullDataController.GetWindDirection());
        assertEquals(1.8, nullDataController.GetWindSpd());
        assertEquals(1.23, nullDataController.Get24HrRain());
        assertEquals(56, (int)nullDataController.GetHumidity());
        assertEquals(2.3, nullDataController.GetMaxWind());
        assertEquals(975.97, nullDataController.GetPressure());
    }



    @Test
    public void MesonetGoodDataImperialTests()
    {
        MesonetDataController nullDataController = new MesonetDataController(MesonetModel.NewInstance(kGoodTestString),
                                                                             new Preferences(){
                                                                                 @Override
                                                                                 public UnitPreference GetUnitPreference ()
                                                                                 {
                                                                                     return UnitPreference.kImperial;
                                                                                 }

                                                                                 @Override
                                                                                 public Observable GetObservable() {
                                                                                     return null;
                                                                                 }
                                                                             });

        assertEquals(39.2, nullDataController.GetTemp());
        assertEquals(36.36946, MathMethods.GetInstance().Round(nullDataController.GetApparentTemp(), 5));
        assertEquals(24.8608, MathMethods.GetInstance().Round(nullDataController.GetDewPoint(), 5));
        assertEquals(UnitConverter.CompassDirections.SSE, nullDataController.GetWindDirection());
        assertEquals(4.02649, MathMethods.GetInstance().Round(nullDataController.GetWindSpd(), 5));
        assertEquals(0.04843, MathMethods.GetInstance().Round(nullDataController.Get24HrRain(), 5));
        assertEquals(56, (int)nullDataController.GetHumidity());
        assertEquals(5.14495, MathMethods.GetInstance().Round(nullDataController.GetMaxWind(), 5));
        assertEquals(38.42402, MathMethods.GetInstance().Round(nullDataController.GetPressure(), 5));
    }
}
