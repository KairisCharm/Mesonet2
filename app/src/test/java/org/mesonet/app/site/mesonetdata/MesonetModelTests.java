package org.mesonet.app.site.mesonetdata;


import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MesonetModelTests
{
    private static final String kEmptyTestString = "";

    private static final String kCantParseString = "BadString";

    private static final String kBadNameTestString = "BadSTNM=121," +
            "TIME=1509238500," +
            "RELH=56," +
            "BadTAIR=4.0," +
            "BadWSPD=1.8," +
            "WVEC=1.8," +
            "WDIR=161," +
            "BadWDSD=4.9," +
            "BadWSSD=0.2," +
            "WMAX=2.3," +
            "RAIN=0.13," +
            "BadPRES=975.97," +
            "BadSRAD=0.5," +
            "TA9M=8.8," +
            "WS2M=0.9," +
            "BadTS10=14.2," +
            "BadTB10=12.1," +
            "TS05=13.7," +
            "TS25=15.6," +
            "BadTS60=18.5," +
            "BadTR05=1.6," +
            "TR25=1.5," +
            "TR60=1.5," +
            "BadRAIN_24H=1.23," +
            "BadRAIN_24=1.23," +
            "STID=NRMN," +
            "YEAR=2017," +
            "BadMONTH=10," +
            "BadDAY=29," +
            "HOUR=13," +
            "MINUTE=55," +
            "BadSECOND=45";

    private static final String kBadValuesTestString = "STNM=121," +
            "TIME=BadValue," +
            "RELH=BadValue," +
            "TAIR=4.0," +
            "WSPD=1.8," +
            "WVEC=BadValue," +
            "WDIR=BadValue," +
            "WDSD=4.9," +
            "WSSD=0.2," +
            "WMAX=BadValue," +
            "RAIN=BadValue," +
            "PRES=975.97," +
            "SRAD=0.5," +
            "TA9M=BadValue," +
            "WS2M=BadValue," +
            "TS10=14.2," +
            "TB10=12.1," +
            "TS05=BadValue," +
            "TS25=BadValue," +
            "TS60=18.5," +
            "TR05=1.6," +
            "TR25=BadValue," +
            "TR60=BadValue," +
            "RAIN_24H=1.23," +
            "RAIN_24=1.23," +
            "STID=NRMN," +
            "YEAR=BadValue," +
            "MONTH=BadValue," +
            "DAY=29," +
            "HOUR=13," +
            "MINUTE=BadValue," +
            "SECOND=BadValue";

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



    @Test
    public void ParseUnparsableTests() throws IllegalAccessException, InstantiationException
    {
        assertTrue(AllFieldsEqualToNew(MesonetModel.NewInstance(null)));
        assertTrue(AllFieldsEqualToNew(MesonetModel.NewInstance(kEmptyTestString)));
        assertTrue(AllFieldsEqualToNew(MesonetModel.NewInstance(kCantParseString)));
    }



    @Test
    public void ParseBadNamesStringTests()
    {
        MesonetModel testModel = MesonetModel.NewInstance(kBadNameTestString);

        assertEquals(null, testModel.STNM);
        assertEquals(1509238500, (long)testModel.TIME);
        assertEquals(56.0, testModel.RELH);
        assertEquals(null, testModel.TAIR);
        assertEquals(null, testModel.WSPD);
        assertEquals(1.8, testModel.WVEC);
        assertEquals(161.0, testModel.WDIR);
        assertEquals(null, testModel.WDSD);
        assertEquals(null, testModel.WSSD);
        assertEquals(2.3, testModel.WMAX);
        assertEquals(0.13, testModel.RAIN);
        assertEquals(null, testModel.PRES);
        assertEquals(null, testModel.SRAD);
        assertEquals(8.8, testModel.TA9M);
        assertEquals(0.9, testModel.WS2M);
        assertEquals(null, testModel.TS10);
        assertEquals(null, testModel.TB10);
        assertEquals(13.7, testModel.TS05);
        assertEquals(15.6, testModel.TS25);
        assertEquals(null, testModel.TS60);
        assertEquals(null, testModel.TR05);
        assertEquals(1.5, testModel.TR25);
        assertEquals(1.5, testModel.TR60);
        assertEquals(null, testModel.RAIN_24H);
        assertEquals(null, testModel.RAIN_24);
        assertEquals("NRMN", testModel.STID);
        assertEquals(2017, (int)testModel.YEAR);
        assertEquals(null, testModel.MONTH);
        assertEquals(null, testModel.DAY);
        assertEquals(13, (int)testModel.HOUR);
        assertEquals(55, (int)testModel.MINUTE);
        assertEquals(null, testModel.SECOND);
    }



    @Test
    public void ParseBadValuesStringTests()
    {
        MesonetModel testModel = MesonetModel.NewInstance(kBadValuesTestString);

        assertEquals(121, (int)testModel.STNM);
        assertEquals(null, testModel.TIME);
        assertEquals(null, testModel.RELH);
        assertEquals(4.0, testModel.TAIR);
        assertEquals(1.8, testModel.WSPD);
        assertEquals(null, testModel.WVEC);
        assertEquals(null, testModel.WDIR);
        assertEquals(4.9, testModel.WDSD);
        assertEquals(0.2, testModel.WSSD);
        assertEquals(null, testModel.WMAX);
        assertEquals(null, testModel.RAIN);
        assertEquals(975.97, testModel.PRES);
        assertEquals(0.5, testModel.SRAD);
        assertEquals(null, testModel.TA9M);
        assertEquals(null, testModel.WS2M);
        assertEquals(14.2, testModel.TS10);
        assertEquals(12.1, testModel.TB10);
        assertEquals(null, testModel.TS05);
        assertEquals(null, testModel.TS25);
        assertEquals(18.5, testModel.TS60);
        assertEquals(1.6, testModel.TR05);
        assertEquals(null, testModel.TR25);
        assertEquals(null, testModel.TR60);
        assertEquals(1.23, testModel.RAIN_24H);
        assertEquals(1.23, testModel.RAIN_24);
        assertEquals("NRMN", testModel.STID);
        assertEquals(null, testModel.YEAR);
        assertEquals(null, testModel.MONTH);
        assertEquals(29, (int)testModel.DAY);
        assertEquals(13, (int)testModel.HOUR);
        assertEquals(null, testModel.MINUTE);
        assertEquals(null, testModel.SECOND);
    }



    @Test
    public void ParseGoodStringTests()
    {
        MesonetModel testModel = MesonetModel.NewInstance(kGoodTestString);

        assertEquals(121, (int)testModel.STNM);
        assertEquals(1509238500, (long)testModel.TIME);
        assertEquals(56.0, testModel.RELH);
        assertEquals(4.0, testModel.TAIR);
        assertEquals(1.8, testModel.WSPD);
        assertEquals(1.8, testModel.WVEC);
        assertEquals(161.0, testModel.WDIR);
        assertEquals(4.9, testModel.WDSD);
        assertEquals(0.2, testModel.WSSD);
        assertEquals(2.3, testModel.WMAX);
        assertEquals(0.13, testModel.RAIN);
        assertEquals(975.97, testModel.PRES);
        assertEquals(0.5, testModel.SRAD);
        assertEquals(8.8, testModel.TA9M);
        assertEquals(0.9, testModel.WS2M);
        assertEquals(14.2, testModel.TS10);
        assertEquals(12.1, testModel.TB10);
        assertEquals(13.7, testModel.TS05);
        assertEquals(15.6, testModel.TS25);
        assertEquals(18.5, testModel.TS60);
        assertEquals(1.6, testModel.TR05);
        assertEquals(1.5, testModel.TR25);
        assertEquals(1.5, testModel.TR60);
        assertEquals(1.23, testModel.RAIN_24H);
        assertEquals(1.23, testModel.RAIN_24);
        assertEquals("NRMN", testModel.STID);
        assertEquals(2017, (int)testModel.YEAR);
        assertEquals(10, (int)testModel.MONTH);
        assertEquals(29, (int)testModel.DAY);
        assertEquals(13, (int)testModel.HOUR);
        assertEquals(55, (int)testModel.MINUTE);
        assertEquals(45, (int)testModel.SECOND);
    }



    private boolean AllFieldsEqualToNew(MesonetModel inTestModel) throws IllegalAccessException
    {
        MesonetModel baseModel = new MesonetModel();

        Field[] fields = MesonetModel.class.getDeclaredFields();

        for(int i = 0; i < fields.length; i++)
        {
            if(fields[i].get(inTestModel) == null && fields[i].get(baseModel) == null)
                continue;
            if(fields[i].get(inTestModel) == null || fields[i].get(baseModel) == null)
                return false;

            if(!fields[i].get(inTestModel).equals(fields[i].get(baseModel))) {
                System.out.print(fields[i].getName() + ": " + fields[i].get(inTestModel).toString() + " vs. " + fields[i].get(baseModel).toString());
                return false;
            }
        }

        return true;
    }
}
