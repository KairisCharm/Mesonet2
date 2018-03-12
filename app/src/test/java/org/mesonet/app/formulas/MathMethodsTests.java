package org.mesonet.app.formulas;


import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;

public class MathMethodsTests
{
    @Inject
    MathMethods mMathMethods;
    
    @Inject
    public MathMethodsTests(){}
    
    @Test
    public void RoundTests()
    {
        assertEquals(null, mMathMethods.Round(null, 0));
        assertEquals(null, mMathMethods.Round(null, -2));
        assertEquals(null, mMathMethods.Round(null, 3));

        assertEquals(0.0, mMathMethods.Round(0.0, 0));
        assertEquals(0.0, mMathMethods.Round(0.0, -2));
        assertEquals(0.0, mMathMethods.Round(0.0, 3));

        assertEquals(1.0, mMathMethods.Round(1.23, 0));
        assertEquals(0.0, mMathMethods.Round(1.23, -2));
        assertEquals(1.23, mMathMethods.Round(1.23, 3));

        assertEquals(5.0, mMathMethods.Round(4.5678, 0));
        assertEquals(0.0, mMathMethods.Round(4.5678, -2));
        assertEquals(4.568, mMathMethods.Round(4.5678, 3));

        assertEquals(-1.0, mMathMethods.Round(-1.23, 0));
        assertEquals(0.0, mMathMethods.Round(-1.23, -2));
        assertEquals(-1.23, mMathMethods.Round(-1.23, 3));

        assertEquals(-5.0, mMathMethods.Round(-4.5678, 0));
        assertEquals(0.0, mMathMethods.Round(-4.5678, -2));
        assertEquals(-4.568, mMathMethods.Round(-4.5678, 3));
    }
}
