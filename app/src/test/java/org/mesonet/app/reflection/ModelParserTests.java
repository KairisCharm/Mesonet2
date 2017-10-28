package org.mesonet.app.reflection;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;



public class ModelParserTests
{
    private static final String kEmptyTestString = "";

    private static final String kCantParseString = "BadString";

    private static final String kBadNameTestString = "mBadByteTest1=1," +
                                                     "mByteTest1=2," +
                                                     "mByteObjTest1=3," +
                                                     "mBadByteObjTest2=4," +
                                                     "mBadShortTest1=5," +
                                                     "mShortTest2=6," +
                                                     "mShortObjTest1=7," +
                                                     "mBadShortObjTest2=8," +
                                                     "mBadIntTest1=9," +
                                                     "mIntTest2=10," +
                                                     "mIntObjTest1=11," +
                                                     "mBadIntObjTest2=12," +
                                                     "mBadLongTest1=13," +
                                                     "mLongTest2=14," +
                                                     "mLongObjTest1=15," +
                                                     "mBadLongObjTest2=16," +
                                                     "mBadFloatTest1=78.901," +
                                                     "mFloatTest2=23.456," +
                                                     "mFloatObjTest1=7.890," +
                                                     "mBadFloatObjTest2=1.234," +
                                                     "mBadDoubleTest1=5.678," +
                                                     "mDoubleTest2=9.012," +
                                                     "mDoubleObjTest1=3.456," +
                                                     "mBadDoubleObjTest2=789.012," +
                                                     "mBadCharTest1=a," +
                                                     "mCharTest2=b," +
                                                     "mCharObjTest1=C," +
                                                     "mBadCharObjTest2=D," +
                                                     "mBadBooleanTest1=true," +
                                                     "mBooleanTest2=false," +
                                                     "mBooleanObjTest1=true," +
                                                     "mBadBooleanObjTest2=false," +
                                                     "mBadNumberTest1=90.12345," +
                                                     "mNumberTest2=678.90," +
                                                     "mStringTest1=StringTest1," +
                                                     "mBadStringTest2=StringTest2";

    private static final String kBadValuesTestString = "mByteTest1=1," +
                                                       "mByteTest1=1234567890," +
                                                       "mByteObjTest1=2," +
                                                       "mByteObjTest2=2345678901," +
                                                       "mShortTest1=3," +
                                                       "mShortTest2=NotANumber," +
                                                       "mShortObjTest1=4," +
                                                       "mShortObjTest2=NotANumber," +
                                                       "mIntTest1=5," +
                                                       "mIntTest2=5678901234," +
                                                       "mIntObjTest1=6," +
                                                       "mIntObjTest2=6789012345," +
                                                       "mLongTest1=7," +
                                                       "mLongTest2=NotANumber," +
                                                       "mLongObjTest1=8," +
                                                       "mLongObjTest2=NotANumber," +
                                                       "mFloatTest1=90.123," +
                                                       "mFloatTest2=340282300000000000000000000000000000000123," +
                                                       "mFloatObjTest1=4.567," +
                                                       "mFloatObjTest2=340282300000000000000000000000000000000456," +
                                                       "mDoubleTest1=890.123," +
                                                       "mDoubleTest2=NotANumber," +
                                                       "mDoubleObjTest1=3.456," +
                                                       "mDoubleObjTest2=NotANumber," +
                                                       "mCharTest1=a," +
                                                       "mCharTest2=bcdef," +
                                                       "mCharObjTest1=C," +
                                                       "mCharObjTest2=DEFGH," +
                                                       "mBooleanTest1=true," +
                                                       "mBooleanTest2=NotABoolean," +
                                                       "mBooleanObjTest1=false," +
                                                       "mBooleanObjTest2=NotABoolean," +
                                                       "mNumberTest1=90.12345," +
                                                       "mNumberTest2=NotANumber," +
                                                       "mStringTest1=StringTest1," +
                                                       "mStringTest2=";

    private static final String kGoodTestString = "mByteTest1=1," +
                                                  "mByteTest1=2," +
                                                  "mByteObjTest1=3," +
                                                  "mByteObjTest2=4," +
                                                  "mShortTest1=5," +
                                                  "mShortTest2=6," +
                                                  "mShortObjTest1=7," +
                                                  "mShortObjTest2=8," +
                                                  "mIntTest1=9," +
                                                  "mIntTest2=10," +
                                                  "mIntObjTest1=11," +
                                                  "mIntObjTest2=12," +
                                                  "mLongTest1=13," +
                                                  "mLongTest2=14," +
                                                  "mLongObjTest1=15," +
                                                  "mLongObjTest2=16," +
                                                  "mFloatTest1=78.901," +
                                                  "mFloatTest2=23.456," +
                                                  "mFloatObjTest1=7.890," +
                                                  "mFloatObjTest2=1.234," +
                                                  "mDoubleTest1=5.678," +
                                                  "mDoubleTest2=9.012," +
                                                  "mDoubleObjTest1=3.456," +
                                                  "mDoubleObjTest2=789.012," +
                                                  "mCharTest1=a," +
                                                  "mCharTest2=b," +
                                                  "mCharObjTest1=C," +
                                                  "mCharObjTest2=D," +
                                                  "mBooleanTest1=true," +
                                                  "mBooleanTest2=false," +
                                                  "mBooleanObjTest1=true," +
                                                  "mBooleanObjTest2=false," +
                                                  "mNumberTest1=90.12345," +
                                                  "mNumberTest2=678.90," +
                                                  "mStringTest1=StringTest1," +
                                                  "mStringTest2=StringTest2";

    class ModelParserTestModel
    {
        byte mByteTest1;
        byte mByteTest2;
        Byte mByteObjTest1;
        Byte mByteObjTest2;
        short mShortTest1;
        short mShortTest2;
        Short mShortObjTest1;
        Short mShortObjTest2;
        int mIntTest1;
        int mIntTest2;
        Integer mIntObjTest1;
        Integer mIntObjTest2;
        long mLongTest1;
        long mLongTest2;
        Long mLongObjTest1;
        Long mLongObjTest2;
        float mFloatTest1;
        float mFloatTest2;
        Float mFloatObjTest1;
        Float mFloatObjTest2;
        double mDoubleTest1;
        double mDoubleTest2;
        Double mDoubleObjTest1;
        Double mDoubleObjTest2;
        char mCharTest1;
        char mCharTest2;
        Character mCharObjTest1;
        Character mCharObjTest2;
        boolean mBooleanTest1;
        boolean mBooleanTest2;
        Boolean mBooleanObjTest1;
        Boolean mBooleanObjTest2;
        Number mNumberTest1;
        Number mNumberTest2;
        String mStringTest1;
        String mStringTest2;
    }



    @Test
    public void ParseNullTests()
    {
        ModelParser.GetInstance().Parse(null, null);
        ModelParser.GetInstance().Parse(null, null);
    }



    @Test
    public void ParseNullObjTests()
    {
        assertEquals(null, ModelParser.GetInstance().Parse(null, null));
        assertEquals(null, ModelParser.GetInstance().Parse(null, kEmptyTestString));
        assertEquals(null, ModelParser.GetInstance().Parse(null, kCantParseString));
        assertEquals(null, ModelParser.GetInstance().Parse(null, kBadNameTestString));
        assertEquals(null, ModelParser.GetInstance().Parse(null, kBadValuesTestString));
        assertEquals(null, ModelParser.GetInstance().Parse(null, kGoodTestString));
    }



    @Test
    public void ParseUnparsableStringTests()
    {
        ModelParserTestModel testModel = new ModelParserTestModel();
        assertEquals(testModel, ModelParser.GetInstance().Parse(ModelParserTestModel.class, null));
        assertEquals(testModel, ModelParser.GetInstance().Parse(ModelParserTestModel.class, kEmptyTestString));
        assertEquals(testModel, ModelParser.GetInstance().Parse(ModelParserTestModel.class, kCantParseString));
    }



    @Test
    public void ParseBadNamesStringTests()
    {
        ModelParserTestModel testModel = ModelParser.GetInstance().Parse(ModelParserTestModel.class, kBadNameTestString);

        assertTrue(testModel.mByteTest1 == 0);
        assertTrue(testModel.mByteTest2 == 2);
        assertTrue(testModel.mByteObjTest1 == 3);
        assertTrue(testModel.mByteObjTest2 == null);
        assertTrue(testModel.mShortTest1 == 0);
        assertTrue(testModel.mShortTest2 == 6);
        assertTrue(testModel.mShortObjTest1 == 7);
        assertTrue(testModel.mShortObjTest2 == null);
        assertTrue(testModel.mIntTest1 == 0);
        assertTrue(testModel.mIntTest2 == 10);
        assertTrue(testModel.mIntObjTest1 == 11);
        assertTrue(testModel.mIntObjTest2 == null);
        assertTrue(testModel.mLongTest1 == 0);
        assertTrue(testModel.mLongTest2 == 14);
        assertTrue(testModel.mLongObjTest1 == 15);
        assertTrue(testModel.mLongObjTest2 == null);
        assertTrue(testModel.mFloatTest1 == 0f);
        assertTrue(testModel.mFloatTest2 == 23.456f);
        assertTrue(testModel.mFloatObjTest1 == 7.890);
        assertTrue(testModel.mFloatObjTest2 == null);
        assertTrue(testModel.mDoubleTest1 == 0.0);
        assertTrue(testModel.mDoubleTest2 == 9.012);
        assertTrue(testModel.mDoubleObjTest1 == 3.456);
        assertTrue(testModel.mDoubleObjTest2 == null);
        assertTrue(testModel.mCharTest1 == '\0');
        assertTrue(testModel.mCharTest2 == 'b');
        assertTrue(testModel.mCharObjTest1 == 'C');
        assertTrue(testModel.mCharObjTest2 == null);
        assertFalse(testModel.mBooleanTest1);
        assertFalse(testModel.mBooleanTest2);
        assertTrue(testModel.mBooleanObjTest1);
        assertTrue(testModel.mBooleanObjTest2 == null);
        assertTrue(testModel.mNumberTest1 == null);
        assertTrue(testModel.mNumberTest2.doubleValue() == 678.9);
        assertEquals(testModel.mStringTest1, "StringTest1");
        assertEquals(testModel.mStringTest2, null);
    }



    @Test
    public void ParseBadValuesStringTests()
    {
        ModelParserTestModel testModel = ModelParser.GetInstance().Parse(ModelParserTestModel.class, kBadValuesTestString);

        assertTrue(testModel.mByteTest1 == 1);
        assertTrue(testModel.mByteTest2 == 0);
        assertTrue(testModel.mByteObjTest1 == 2);
        assertTrue(testModel.mByteObjTest2 == null);
        assertTrue(testModel.mShortTest1 == 3);
        assertTrue(testModel.mShortTest2 == 0);
        assertTrue(testModel.mShortObjTest1 == 4);
        assertTrue(testModel.mShortObjTest2 == null);
        assertTrue(testModel.mIntTest1 == 5);
        assertTrue(testModel.mIntTest2 == 0);
        assertTrue(testModel.mIntObjTest1 == 6);
        assertTrue(testModel.mIntObjTest2 == null);
        assertTrue(testModel.mLongTest1 == 7);
        assertTrue(testModel.mLongTest2 == 0);
        assertTrue(testModel.mLongObjTest1 == 8);
        assertTrue(testModel.mLongObjTest2 == null);
        assertTrue(testModel.mFloatTest1 == 90.123f);
        assertTrue(testModel.mFloatTest2 == 0f);
        assertTrue(testModel.mFloatObjTest1 == 4.567f);
        assertTrue(testModel.mFloatObjTest2 == null);
        assertTrue(testModel.mDoubleTest1 == 890.123);
        assertTrue(testModel.mDoubleTest2 == 0.0);
        assertTrue(testModel.mDoubleObjTest1 == 3.456);
        assertTrue(testModel.mDoubleObjTest2 == null);
        assertTrue(testModel.mCharTest1 == 'a');
        assertTrue(testModel.mCharTest2 == 'b');
        assertTrue(testModel.mCharObjTest1 == 'C');
        assertTrue(testModel.mCharObjTest2 == 'D');
        assertTrue(testModel.mBooleanTest1);
        assertFalse(testModel.mBooleanTest2);
        assertFalse(testModel.mBooleanObjTest1);
        assertTrue(testModel.mBooleanObjTest2 == null);
        assertTrue(testModel.mNumberTest1.doubleValue() == 90.12345);
        assertTrue(testModel.mNumberTest2 == null);
        assertEquals(testModel.mStringTest1, "StringTest1");
        assertEquals(testModel.mStringTest2, null);
    }



    @Test
    public void ParseGoodStringTests()
    {
        ModelParserTestModel testModel = ModelParser.GetInstance().Parse(ModelParserTestModel.class, kGoodTestString);

        assertTrue(testModel.mByteTest1 == 1);
        assertTrue(testModel.mByteTest2 == 2);
        assertTrue(testModel.mByteObjTest1 == 3);
        assertTrue(testModel.mByteObjTest2 == 4);
        assertTrue(testModel.mShortTest1 == 5);
        assertTrue(testModel.mShortTest2 == 6);
        assertTrue(testModel.mShortObjTest1 == 7);
        assertTrue(testModel.mShortObjTest2 == 8);
        assertTrue(testModel.mIntTest1 == 9);
        assertTrue(testModel.mIntTest2 == 10);
        assertTrue(testModel.mIntObjTest1 == 11);
        assertTrue(testModel.mIntObjTest2 == 12);
        assertTrue(testModel.mLongTest1 == 13);
        assertTrue(testModel.mLongTest2 == 14);
        assertTrue(testModel.mLongObjTest1 == 15);
        assertTrue(testModel.mLongObjTest2 == 16);
        assertTrue(testModel.mFloatTest1 == 78.901f);
        assertTrue(testModel.mFloatTest2 == 23.456f);
        assertTrue(testModel.mFloatObjTest1 == 7.89f);
        assertTrue(testModel.mFloatObjTest2 == 1.234f);
        assertTrue(testModel.mDoubleTest1 == 5.678);
        assertTrue(testModel.mDoubleTest2 == 9.012);
        assertTrue(testModel.mDoubleObjTest1 == 3.456);
        assertTrue(testModel.mDoubleObjTest2 == 789.012);
        assertTrue(testModel.mCharTest1 == 'a');
        assertTrue(testModel.mCharTest2 == 'b');
        assertTrue(testModel.mCharObjTest1 == 'C');
        assertTrue(testModel.mCharObjTest2 == 'D');
        assertTrue(testModel.mBooleanTest1);
        assertFalse(testModel.mBooleanTest2);
        assertTrue(testModel.mBooleanObjTest1);
        assertFalse(testModel.mBooleanObjTest2);
        assertTrue(testModel.mNumberTest1.doubleValue() == 90.12345);
        assertTrue(testModel.mNumberTest2.doubleValue() == 678.90);
        assertEquals(testModel.mStringTest1, "StringTest1");
        assertEquals(testModel.mStringTest2, "StringTest2");
    }
}
