package org.mesonet.app.formulas;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UnitConversionTests
{
    @Test
    public void CelsiusToFahrenheitTests()
    {
        assertEquals(null, UnitConverter.GetInstance().CelsiusToFahrenheit(null));
        assertEquals(32.0, UnitConverter.GetInstance().CelsiusToFahrenheit(0));
        assertEquals(33.8, UnitConverter.GetInstance().CelsiusToFahrenheit(1));
        assertEquals(-7.3, MathMethods.GetInstance().Round(UnitConverter.GetInstance().CelsiusToFahrenheit(-21.8333333), 5));
        assertEquals(55.4, MathMethods.GetInstance().Round(UnitConverter.GetInstance().CelsiusToFahrenheit(13), 5));
        assertEquals(104.3, MathMethods.GetInstance().Round(UnitConverter.GetInstance().CelsiusToFahrenheit(40.1666667), 5));
    }



    @Test
    public void FahrenheitToCelsiusTests()
    {
        assertEquals(null, UnitConverter.GetInstance().FahrenheitToCelsius(null));
        assertEquals(0.0, UnitConverter.GetInstance().FahrenheitToCelsius(32.0));
        assertEquals(1.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().FahrenheitToCelsius(33.8), 5));
        assertEquals(-21.83333, MathMethods.GetInstance().Round(UnitConverter.GetInstance().FahrenheitToCelsius(-7.3), 5));
        assertEquals(13.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().FahrenheitToCelsius(55.4), 5));
        assertEquals(40.16667, MathMethods.GetInstance().Round(UnitConverter.GetInstance().FahrenheitToCelsius(104.3), 5));
    }



    @Test
    public void MpsToMphTests()
    {
        assertEquals(null, UnitConverter.GetInstance().MpsToMph(null));
        assertEquals(0.0, UnitConverter.GetInstance().MpsToMph(0));
        assertEquals(2.23694, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MpsToMph(1), 5));
        assertEquals(-57.42215, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MpsToMph(-25.67), 5));
        assertEquals(102.00429, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MpsToMph(45.6), 5));
    }



    @Test
    public void MpsToKmphTests()
    {
        assertEquals(null, UnitConverter.GetInstance().MpsToKmph(null));
        assertEquals(0.0, UnitConverter.GetInstance().MpsToKmph(0));
        assertEquals(3.6, UnitConverter.GetInstance().MpsToKmph(1));
        assertEquals(-92.412, UnitConverter.GetInstance().MpsToKmph(-25.67));
        assertEquals(164.16, UnitConverter.GetInstance().MpsToKmph(45.6));
    }



    @Test
    public void MmToInTests()
    {
        assertEquals(null, UnitConverter.GetInstance().MmToIn(null));
        assertEquals(0.0, UnitConverter.GetInstance().MmToIn(0));
        assertEquals(0.03937, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MmToIn(1), 5));
        assertEquals(-1.01063, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MmToIn(-25.67), 5));
        assertEquals(1.79528, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MmToIn(45.6), 5));
    }



    @Test
    public void MmHgToInHgTests()
    {
        assertEquals(null, UnitConverter.GetInstance().MmHgToInHg(null));
        assertEquals(0.0, UnitConverter.GetInstance().MmHgToInHg(0));
        assertEquals(23.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MmHgToInHg(584.2), 5));
        assertEquals(29.92, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MmHgToInHg(759.968), 5));
        assertEquals(36.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MmHgToInHg(914.400), 5));
    }
}