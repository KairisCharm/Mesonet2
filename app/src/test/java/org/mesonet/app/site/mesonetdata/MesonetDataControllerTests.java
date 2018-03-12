package org.mesonet.app.site.mesonetdata;


import org.junit.Test;
import org.mesonet.app.formulas.MathMethods;
import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.userdata.Preferences;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.mesonet.app.site.mesonetdata.MesonetModelTests.kGoodTestString;

public class MesonetDataControllerTests
{
    private int mObserverCheck;
    private Observer mObserver = new Observer(){
        @Override
        public void update (Observable inObservable,
                            Object inO)
        {
            mObserverCheck++;
        }
    };


    @Inject
    MathMethods mMathMethods;



    @Test
    public void MesonetNullDataTests()
    {
        mObserverCheck = 0;
        BaseMesonetDataController nullDataController = new MesonetDataController(null, null);

        assertEquals(null, nullDataController.GetTemp());
        assertEquals(null, nullDataController.GetApparentTemp());
        assertEquals(null, nullDataController.GetDewpoint());
        assertEquals(null, nullDataController.GetWindDirection());
        assertEquals(null, nullDataController.GetWindSpd());
        assertEquals(null, nullDataController.Get24HrRain());
        assertEquals(null, nullDataController.GetHumidity());
        assertEquals(null, nullDataController.GetMaxWind());
        assertEquals(null, nullDataController.GetPressure());

        assertEquals(null, nullDataController.GetUnitPreference());

        nullDataController.addObserver(mObserver);

        nullDataController.SetData("");
        assertEquals(1, mObserverCheck);
        nullDataController.SetData(new MesonetModel());
        assertEquals(2, mObserverCheck);
    }



    @Test
    public void MesonetEmptyDataTests()
    {
        BaseMesonetDataController emtpyDataController = new MesonetDataController(MesonetModel.NewInstance(""), null);

        assertEquals(null, emtpyDataController.GetTemp());
        assertEquals(null, emtpyDataController.GetApparentTemp());
        assertEquals(null, emtpyDataController.GetDewpoint());
        assertEquals(null, emtpyDataController.GetWindDirection());
        assertEquals(null, emtpyDataController.GetWindSpd());
        assertEquals(null, emtpyDataController.Get24HrRain());
        assertEquals(null, emtpyDataController.GetHumidity());
        assertEquals(null, emtpyDataController.GetMaxWind());
        assertEquals(null, emtpyDataController.GetPressure());

        assertEquals(null, emtpyDataController.GetUnitPreference());

        emtpyDataController.addObserver(mObserver);

        emtpyDataController.SetData("");
        assertEquals(1, mObserverCheck);
        emtpyDataController.SetData(new MesonetModel());
        assertEquals(2, mObserverCheck);
    }



    @Test
    public void MesonetGoodDataNoPreferenceTests()
    {
        BaseMesonetDataController noPreferenceDataController = new MesonetDataController(MesonetModel.NewInstance(kGoodTestString), null);

        assertEquals(4.0, noPreferenceDataController.GetTemp());
        assertEquals(2.42748, mMathMethods.Round(noPreferenceDataController.GetApparentTemp(), 5));
        assertEquals(-3.96622, mMathMethods.Round(noPreferenceDataController.GetDewpoint(), 5));
        assertEquals(UnitConverter.CompassDirections.SSE, noPreferenceDataController.GetWindDirection());
        assertEquals(1.8, noPreferenceDataController.GetWindSpd());
        assertEquals(1.23, noPreferenceDataController.Get24HrRain());
        assertEquals(56, (int)noPreferenceDataController.GetHumidity());
        assertEquals(2.3, noPreferenceDataController.GetMaxWind());
        assertEquals(975.97, noPreferenceDataController.GetPressure());

        assertEquals(null, noPreferenceDataController.GetUnitPreference());

        noPreferenceDataController.addObserver(mObserver);

        noPreferenceDataController.SetData("");
        assertEquals(1, mObserverCheck);
        noPreferenceDataController.SetData(new MesonetModel());
        assertEquals(2, mObserverCheck);
    }



    @Test
    public void MesonetGoodDataMetricTests()
    {
        BaseMesonetDataController metricDataController = new BaseMesonetDataController(MesonetModel.NewInstance(kGoodTestString),
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

        assertEquals(4.0, metricDataController.GetTemp());
        assertEquals(2.42748, mMathMethods.Round(metricDataController.GetApparentTemp(), 5));
        assertEquals(-3.96622, mMathMethods.Round(metricDataController.GetDewpoint(), 5));
        assertEquals(UnitConverter.CompassDirections.SSE, metricDataController.GetWindDirection());
        assertEquals(1.8, metricDataController.GetWindSpd());
        assertEquals(1.23, metricDataController.Get24HrRain());
        assertEquals(56, (int)metricDataController.GetHumidity());
        assertEquals(2.3, metricDataController.GetMaxWind());
        assertEquals(975.97, metricDataController.GetPressure());

        metricDataController.addObserver(mObserver);

        assertEquals(Preferences.UnitPreference.kMetric, metricDataController.GetUnitPreference());

        metricDataController.SetData("");
        assertEquals(1, mObserverCheck);
        metricDataController.SetData(new MesonetModel());
        assertEquals(2, mObserverCheck);
    }



    @Test
    public void MesonetGoodDataImperialTests()
    {
        BaseMesonetDataController imperialDataController = new BaseMesonetDataController(MesonetModel.NewInstance(kGoodTestString),
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

        assertEquals(39.2, imperialDataController.GetTemp());
        assertEquals(36.36946, mMathMethods.Round(imperialDataController.GetApparentTemp(), 5));
        assertEquals(24.8608, mMathMethods.Round(imperialDataController.GetDewpoint(), 5));
        assertEquals(UnitConverter.CompassDirections.SSE, imperialDataController.GetWindDirection());
        assertEquals(4.02649, mMathMethods.Round(imperialDataController.GetWindSpd(), 5));
        assertEquals(0.04843, mMathMethods.Round(imperialDataController.Get24HrRain(), 5));
        assertEquals(56, (int)imperialDataController.GetHumidity());
        assertEquals(5.14495, mMathMethods.Round(imperialDataController.GetMaxWind(), 5));
        assertEquals(38.42402, mMathMethods.Round(imperialDataController.GetPressure(), 5));

        imperialDataController.addObserver(mObserver);

        assertEquals(Preferences.UnitPreference.kImperial, imperialDataController.GetUnitPreference());

        imperialDataController.SetData("");
        assertEquals(1, mObserverCheck);
        imperialDataController.SetData(new MesonetModel());
        assertEquals(2, mObserverCheck);
    }



    @Test
    public void MesonetDataControllerUpdatingTests()
    {
        BaseMesonetDataController imperialDataController = new BaseMesonetDataController(MesonetModel.NewInstance(kGoodTestString),
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

        assertEquals(false, imperialDataController.IsUpdating());
        imperialDataController.StartUpdating();
        assertEquals(true, imperialDataController.IsUpdating());
        imperialDataController.StopUpdating();
        assertEquals(false, imperialDataController.IsUpdating());
    }
}
