package org.mesonet.app.reflection;

import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;


public class ModelParserTests
{

    private static final String kEmptyTestString = "";

    private static final String kCantParseString = "BadString";

    private static final String kBadNameTestString = "mBadByteTest1=1," +
                                                     "mByteTest2=2," +
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
                                                       "mByteTest2=1234567890," +
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
                                                       "mFloatTest2=NotANumber," +
                                                       "mFloatObjTest1=4.567," +
                                                       "mFloatObjTest2=NotANumber," +
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
                                                  "mByteTest2=2," +
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

    static class ModelParserTestModel
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
    public void ParseNullStringTests() throws IllegalAccessException
    {
        assertTrue(AllFieldsEqualToNew(ModelParser.GetInstance().Parse(ModelParserTestModel.class, null)));
        assertTrue(AllFieldsEqualToNew(ModelParser.GetInstance().Parse(ModelParserTestModel.class, kEmptyTestString)));
        assertTrue(AllFieldsEqualToNew(ModelParser.GetInstance().Parse(ModelParserTestModel.class, kCantParseString)));
    }



    @Test
    public void ParseBadNamesStringTests()
    {
        ModelParserTestModel testModel = ModelParser.GetInstance().Parse(ModelParserTestModel.class, kBadNameTestString);

        assertEquals(0, testModel.mByteTest1);
        assertEquals(2, testModel.mByteTest2);
        assertEquals(3, (byte)testModel.mByteObjTest1);
        assertEquals(null, testModel.mByteObjTest2);
        assertEquals(0, testModel.mShortTest1);
        assertEquals(6, testModel.mShortTest2);
        assertEquals(7, (short)testModel.mShortObjTest1);
        assertEquals(null, testModel.mShortObjTest2);
        assertEquals(0, testModel.mIntTest1);
        assertEquals(10, testModel.mIntTest2);
        assertEquals(11, (int)testModel.mIntObjTest1);
        assertEquals(null, testModel.mIntObjTest2);
        assertEquals(0, testModel.mLongTest1);
        assertEquals(14, testModel.mLongTest2);
        assertEquals(15, (long)testModel.mLongObjTest1);
        assertEquals(null, testModel.mLongObjTest2);
        assertEquals(0f, testModel.mFloatTest1);
        assertEquals(23.456f, testModel.mFloatTest2);
        assertEquals(7.890f, testModel.mFloatObjTest1);
        assertEquals(null, testModel.mFloatObjTest2);
        assertEquals(0.0, testModel.mDoubleTest1);
        assertEquals(9.012, testModel.mDoubleTest2);
        assertEquals(3.456, testModel.mDoubleObjTest1);
        assertEquals(null, testModel.mDoubleObjTest2);
        assertEquals('\0', testModel.mCharTest1);
        assertEquals('b', testModel.mCharTest2);
        assertEquals('C', (char)testModel.mCharObjTest1);
        assertEquals(null, testModel.mCharObjTest2);
        assertEquals(false, testModel.mBooleanTest1);
        assertEquals(false, testModel.mBooleanTest2);
        assertEquals(true, (boolean)testModel.mBooleanObjTest1);
        assertEquals(null, testModel.mBooleanObjTest2);
        assertEquals(null, testModel.mNumberTest1);
        assertEquals(678.9, testModel.mNumberTest2.doubleValue());
        assertEquals("StringTest1", testModel.mStringTest1 );
        assertEquals(null, testModel.mStringTest2);
    }



    @Test
    public void ParseBadValuesStringTests()
    {
        ModelParserTestModel testModel = ModelParser.GetInstance().Parse(ModelParserTestModel.class, kBadValuesTestString);

        assertEquals(1, testModel.mByteTest1);
        assertEquals(0, testModel.mByteTest2);
        assertEquals(2, (byte)testModel.mByteObjTest1);
        assertEquals(null, testModel.mByteObjTest2);
        assertEquals(3, testModel.mShortTest1);
        assertEquals(0, testModel.mShortTest2);
        assertEquals(4, (short)testModel.mShortObjTest1);
        assertEquals(null, testModel.mShortObjTest2);
        assertEquals(5, testModel.mIntTest1);
        assertEquals(0, testModel.mIntTest2);
        assertEquals(6, (int)testModel.mIntObjTest1);
        assertEquals(null, testModel.mIntObjTest2);
        assertEquals(7, testModel.mLongTest1);
        assertEquals(0, testModel.mLongTest2);
        assertEquals(8, (long)testModel.mLongObjTest1);
        assertEquals(null, testModel.mLongObjTest2);
        assertEquals(90.123f, testModel.mFloatTest1);
        assertEquals(0f, testModel.mFloatTest2);
        assertEquals(4.567f, testModel.mFloatObjTest1);
        assertEquals(null, testModel.mFloatObjTest2);
        assertEquals(890.123, testModel.mDoubleTest1);
        assertEquals(0.0, testModel.mDoubleTest2);
        assertEquals(3.456, testModel.mDoubleObjTest1);
        assertEquals(null, testModel.mDoubleObjTest2);
        assertEquals('a', testModel.mCharTest1);
        assertEquals('b', testModel.mCharTest2);
        assertEquals('C', (char)testModel.mCharObjTest1);
        assertEquals('D', (char)testModel.mCharObjTest2);
        assertEquals(true, testModel.mBooleanTest1);
        assertEquals(false, testModel.mBooleanTest2);
        assertEquals(false, (boolean)testModel.mBooleanObjTest1);
        assertEquals(false, (boolean)testModel.mBooleanObjTest2);
        assertEquals(90.12345, testModel.mNumberTest1.doubleValue());
        assertEquals(null, testModel.mNumberTest2);
        assertEquals("StringTest1", testModel.mStringTest1);
        assertEquals(null, testModel.mStringTest2);
    }



    @Test
    public void ParseGoodStringTests()
    {
        ModelParserTestModel testModel = ModelParser.GetInstance().Parse(ModelParserTestModel.class, kGoodTestString);

        assertEquals(1, testModel.mByteTest1);
        assertEquals(2, testModel.mByteTest2);
        assertEquals(3, (byte)testModel.mByteObjTest1);
        assertEquals(4, (byte)testModel.mByteObjTest2);
        assertEquals(5, testModel.mShortTest1);
        assertEquals(6, testModel.mShortTest2);
        assertEquals(7, (short)testModel.mShortObjTest1);
        assertEquals(8,(short)testModel.mShortObjTest2);
        assertEquals(9,testModel.mIntTest1);
        assertEquals(10, testModel.mIntTest2);
        assertEquals(11, (int)testModel.mIntObjTest1);
        assertEquals(12, (int)testModel.mIntObjTest2);
        assertEquals(13, testModel.mLongTest1);
        assertEquals(14, testModel.mLongTest2);
        assertEquals(15, (long)testModel.mLongObjTest1);
        assertEquals(16, (long)testModel.mLongObjTest2);
        assertEquals(78.901f, testModel.mFloatTest1);
        assertEquals(23.456f, testModel.mFloatTest2);
        assertEquals(7.89f, testModel.mFloatObjTest1);
        assertEquals(1.234f, testModel.mFloatObjTest2);
        assertEquals(5.678, testModel.mDoubleTest1);
        assertEquals(9.012, testModel.mDoubleTest2);
        assertEquals(3.456, testModel.mDoubleObjTest1);
        assertEquals(789.012, testModel.mDoubleObjTest2);
        assertEquals('a', testModel.mCharTest1);
        assertEquals('b', testModel.mCharTest2);
        assertEquals('C', (char)testModel.mCharObjTest1);
        assertEquals('D', (char)testModel.mCharObjTest2);
        assertEquals(true, testModel.mBooleanTest1);
        assertEquals(false, testModel.mBooleanTest2);
        assertEquals(true, (boolean)testModel.mBooleanObjTest1);
        assertEquals(false, (boolean)testModel.mBooleanObjTest2);
        assertEquals(90.12345, testModel.mNumberTest1.doubleValue());
        assertEquals(678.90, testModel.mNumberTest2.doubleValue());
        assertEquals("StringTest1", testModel.mStringTest1);
        assertEquals("StringTest2", testModel.mStringTest2);
    }



    private boolean AllFieldsEqualToNew(ModelParserTestModel inTestModel) throws IllegalAccessException
    {
        ModelParserTestModel baseModel = new ModelParserTestModel();

        Field[] fields = ModelParserTestModel.class.getDeclaredFields();

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
