package org.mesonet.app.formulas;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MathMethodsTests
{
    @Test
    public void RoundTests()
    {
        assertEquals(null, MathMethods.GetInstance().Round(null, 0));
        assertEquals(null, MathMethods.GetInstance().Round(null, -2));
        assertEquals(null, MathMethods.GetInstance().Round(null, 3));

        assertEquals(0.0, MathMethods.GetInstance().Round(0.0, 0));
        assertEquals(0.0, MathMethods.GetInstance().Round(0.0, -2));
        assertEquals(0.0, MathMethods.GetInstance().Round(0.0, 3));

        assertEquals(1.0, MathMethods.GetInstance().Round(1.23, 0));
        assertEquals(0.0, MathMethods.GetInstance().Round(1.23, -2));
        assertEquals(1.23, MathMethods.GetInstance().Round(1.23, 3));

        assertEquals(5.0, MathMethods.GetInstance().Round(4.5678, 0));
        assertEquals(0.0, MathMethods.GetInstance().Round(4.5678, -2));
        assertEquals(4.568, MathMethods.GetInstance().Round(4.5678, 3));

        assertEquals(-1.0, MathMethods.GetInstance().Round(-1.23, 0));
        assertEquals(0.0, MathMethods.GetInstance().Round(-1.23, -2));
        assertEquals(-1.23, MathMethods.GetInstance().Round(-1.23, 3));

        assertEquals(-5.0, MathMethods.GetInstance().Round(-4.5678, 0));
        assertEquals(0.0, MathMethods.GetInstance().Round(-4.5678, -2));
        assertEquals(-4.568, MathMethods.GetInstance().Round(-4.5678, 3));
    }
}
