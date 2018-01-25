package org.mesonet.app.site.mesonetdata;


import org.junit.Test;
import org.mesonet.app.formulas.MathMethods;

import static junit.framework.Assert.assertEquals;

public class DerivedValuesTests
{
    @Test
    public void HeatIndexTests()
    {
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(null, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(null, 53));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(null, 4));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(null, 87));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(-19.4444444, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(0, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(7.22222, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(22.2222, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(28.3333, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(40, null));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(-19.4444444, 53));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(-19.4444444, 4));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(-19.4444444, 87));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(0, 53));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(0, 4));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(0, 87));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(7.22222, 53));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(7.22222, 4));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(7.22222, 87));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(22.2222, 53));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(22.2222, 4));
        assertEquals(null, DerivedValues.GetInstance().GetHeatIndex(22.2222, 87));
        assertEquals(29.1427, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetHeatIndex(28.3333, 53), 5));
        assertEquals(26.90734, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetHeatIndex(28.3333, 4), 5));
        assertEquals(34.23341, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetHeatIndex(28.3333, 87), 5));
        assertEquals(56.98646, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetHeatIndex(40, 53), 5));
        assertEquals(36.21863, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetHeatIndex(40, 4), 5));
        assertEquals(90.7624, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetHeatIndex(40, 87), 5));
    }



    @Test
    public void WindChillTests()
    {
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(null, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(null, 1.3));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(null, 35.7632));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(null, 8.04672));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(-19.4444444, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(0, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(7.22222, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(22.2222, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(28.3333, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(40, null));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(-19.4444444, 1.3));
        assertEquals(-40.43483, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetWindChill(-19.4444444, 35.7632), 5));
        assertEquals(-31.62843, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetWindChill(-19.4444444, 8.04672), 5));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(0, 1.3));
        assertEquals(-11.59195, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetWindChill(0, 35.7632), 5));
        assertEquals(-6.34367, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetWindChill(0, 8.04672), 5));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(7.22222, 1.3));
        assertEquals(-0.87889, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetWindChill(7.22222, 35.7632), 5));
        assertEquals(3.04781, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetWindChill(7.22222, 8.04672), 5));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(22.2222, 1.3));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(22.2222, 35.7632));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(22.2222, 8.04672));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(28.3333, 1.3));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(28.3333, 35.7632));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(28.3333, 8.04672));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(40, 1.3));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(40, 35.7632));
        assertEquals(null, DerivedValues.GetInstance().GetWindChill(40, 8.04672));
    }



    @Test
    public void ApparentTemperatureTests()
    {
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, null, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, null, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, null, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, null, 87));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 1.3, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 1.3, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 1.3, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 1.3, 87));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 35.7632, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 35.7632, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 35.7632, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 35.7632, 87));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 8.04672, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 8.04672, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 8.04672, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(null, 8.04672, 87));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, null, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, null, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, null, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, null, 87));
        assertEquals(-19.4444444, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 1.3, null));
        assertEquals(-19.4444444, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 1.3, 53));
        assertEquals(-19.4444444, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 1.3, 4));
        assertEquals(-19.4444444, DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 1.3, 87));
        assertEquals(-40.43483, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 35.7632, null), 5));
        assertEquals(-40.43483, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 35.7632, 53), 5));
        assertEquals(-40.43483, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 35.7632, 4), 5));
        assertEquals(-40.43483, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 35.7632, 87), 5));
        assertEquals(-31.62843, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 8.04672, null), 5));
        assertEquals(-31.62843, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 8.04672, 53), 5));
        assertEquals(-31.62843, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 8.04672, 4), 5));
        assertEquals(-31.62843, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(-19.4444444, 8.04672, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(0, null, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(0, null, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(0, null, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(0, null, 87));
        assertEquals(0, DerivedValues.GetInstance().GetApparentTemperature(0, 1.3, null));
        assertEquals(0, DerivedValues.GetInstance().GetApparentTemperature(0, 1.3, 53));
        assertEquals(0, DerivedValues.GetInstance().GetApparentTemperature(0, 1.3, 4));
        assertEquals(0, DerivedValues.GetInstance().GetApparentTemperature(0, 1.3, 87));
        assertEquals(-11.59195, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 35.7632, null), 5));
        assertEquals(-11.59195, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 35.7632, 53), 5));
        assertEquals(-11.59195, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 35.7632, 4), 5));
        assertEquals(-11.59195, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 35.7632, 87), 5));
        assertEquals(-6.34367, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 8.04672, null), 5));
        assertEquals(-6.34367, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 8.04672, 53), 5));
        assertEquals(-6.34367, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 8.04672, 4), 5));
        assertEquals(-6.34367, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(0, 8.04672, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(7.22222, null, null));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(7.22222, null, 53));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(7.22222, null, 4));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(7.22222, null, 87));
        assertEquals(7.22222, DerivedValues.GetInstance().GetApparentTemperature(7.22222, 1.3, null));
        assertEquals(7.22222, DerivedValues.GetInstance().GetApparentTemperature(7.22222, 1.3, 53));
        assertEquals(7.22222, DerivedValues.GetInstance().GetApparentTemperature(7.22222, 1.3, 4));
        assertEquals(7.22222, DerivedValues.GetInstance().GetApparentTemperature(7.22222, 1.3, 87));
        assertEquals(-0.87889, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 35.7632, null), 5));
        assertEquals(-0.87889, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 35.7632, 53), 5));
        assertEquals(-0.87889, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 35.7632, 4), 5));
        assertEquals(-0.87889, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 35.7632, 87), 5));
        assertEquals(3.04781, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 8.04672, null), 5));
        assertEquals(3.04781, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 8.04672, 53), 5));
        assertEquals(3.04781, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 8.04672, 4), 5));
        assertEquals(3.04781, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(7.22222, 8.04672, 87), 5));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, null, null));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, null, 53));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, null, 4));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, null, 87));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 1.3, null));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 1.3, 53));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 1.3, 4));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 1.3, 87));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 35.7632, null));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 35.7632, 53));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 35.7632, 4));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 35.7632, 87));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 8.04672, null));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 8.04672, 53));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 8.04672, 4));
        assertEquals(22.2222, DerivedValues.GetInstance().GetApparentTemperature(22.2222, 8.04672, 87));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(28.3333, null, null));
        assertEquals(29.1427, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, null, 53), 5));
        assertEquals(26.90734, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, null, 4), 5));
        assertEquals(34.23341, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, null, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(28.3333, 1.3, null));
        assertEquals(29.1427, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 1.3, 53), 5));
        assertEquals(26.90734, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 1.3, 4), 5));
        assertEquals(34.23341, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 1.3, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(28.3333, 35.7632, null));
        assertEquals(29.1427, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 35.7632, 53), 5));
        assertEquals(26.90734, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 35.7632, 4), 5));
        assertEquals(34.23341, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 35.7632, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(28.3333, 8.04672, null));
        assertEquals(29.1427, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 8.04672, 53), 5));
        assertEquals(26.90734, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 8.04672, 4), 5));
        assertEquals(34.23341, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(28.3333, 8.04672, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(40, null, null));
        assertEquals(56.98646, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, null, 53), 5));
        assertEquals(36.21863, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, null, 4), 5));
        assertEquals(90.7624, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, null, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(40, 1.3, null));
        assertEquals(56.98646, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 1.3, 53), 5));
        assertEquals(36.21863, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 1.3, 4), 5));
        assertEquals(90.7624, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 1.3, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(40, 35.7632, null));
        assertEquals(56.98646, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 35.7632, 53), 5));
        assertEquals(36.21863, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 35.7632, 4), 5));
        assertEquals(90.7624, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 35.7632, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetApparentTemperature(40, 8.04672, null));
        assertEquals(56.98646, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 8.04672, 53), 5));
        assertEquals(36.21863, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 8.04672, 4), 5));
        assertEquals(90.7624, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetApparentTemperature(40, 8.04672, 87), 5));
    }



    @Test
    public void DewPointTests()
    {
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(null, null));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(null, 53));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(null, 4));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(null, 87));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(-19.4444444, null));
        assertEquals(-26.55719, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(-19.4444444, 53), 5));
        assertEquals(-51.27658, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(-19.4444444, 4), 5));
        assertEquals(-21.04545, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(-19.4444444, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(0, null));
        assertEquals(-8.41457, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(0, 53), 5));
        assertEquals(-37.28179, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(0, 4), 5));
        assertEquals(-1.8983, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(0, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(7.22222, null));
        assertEquals(-1.7027, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(7.22222, 53), 5));
        assertEquals(-32.17507, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(7.22222, 4), 5));
        assertEquals(5.2071, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(7.22222, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(22.2222, null));
        assertEquals(12.19117, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(22.2222, 53), 5));
        assertEquals(-21.72258, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(22.2222, 4), 5));
        assertEquals(19.95341, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(22.2222, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(28.3333, null));
        assertEquals(17.83385, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(28.3333, 53), 5));
        assertEquals(-17.52259, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(28.3333, 4), 5));
        assertEquals(25.9569, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(28.3333, 87), 5));
        assertEquals(null, DerivedValues.GetInstance().GetDewPoint(40, null));
        assertEquals(28.57785, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(40, 53), 5));
        assertEquals(-9.59632, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(40, 4), 5));
        assertEquals(37.41128, MathMethods.GetInstance().Round(DerivedValues.GetInstance().GetDewPoint(40, 87), 5));
    }
}
