package org.mesonet.app.formulas;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UnitConversionTests
{
    private DefaultUnits nullUnits = new DefaultUnits() {
        @Override
        public UnitConverter.TempUnits GetDefaultTempUnit() {
            return null;
        }

        @Override
        public UnitConverter.LengthUnits GetDefaultLengthUnit() {
            return null;
        }

        @Override
        public UnitConverter.SpeedUnits GetDefaultSpeedUnit() {
            return null;
        }

        @Override
        public UnitConverter.PressureUnits GetDefaultPressureUnit() {
            return null;
        }
    };



    private DefaultUnits metricUnits = new DefaultUnits() {
        @Override
        public UnitConverter.TempUnits GetDefaultTempUnit() {
            return UnitConverter.TempUnits.kCelsius;
        }

        @Override
        public UnitConverter.LengthUnits GetDefaultLengthUnit() {
            return UnitConverter.LengthUnits.kMm;
        }

        @Override
        public UnitConverter.SpeedUnits GetDefaultSpeedUnit() {
            return UnitConverter.SpeedUnits.kMps;
        }

        @Override
        public UnitConverter.PressureUnits GetDefaultPressureUnit() {
            return UnitConverter.PressureUnits.kMmHg;
        }
    };



    private DefaultUnits imperialUnits = new DefaultUnits() {
        @Override
        public UnitConverter.TempUnits GetDefaultTempUnit() {
            return UnitConverter.TempUnits.kFahrenheit;
        }

        @Override
        public UnitConverter.LengthUnits GetDefaultLengthUnit() {
            return UnitConverter.LengthUnits.kIn;
        }

        @Override
        public UnitConverter.SpeedUnits GetDefaultSpeedUnit() {
            return UnitConverter.SpeedUnits.kMph;
        }

        @Override
        public UnitConverter.PressureUnits GetDefaultPressureUnit() {
            return UnitConverter.PressureUnits.kInHg;
        }
    };



    private DefaultUnits kmphUnits = new DefaultUnits() {
        @Override
        public UnitConverter.TempUnits GetDefaultTempUnit() {
            return UnitConverter.TempUnits.kCelsius;
        }

        @Override
        public UnitConverter.LengthUnits GetDefaultLengthUnit() {
            return UnitConverter.LengthUnits.kMm;
        }

        @Override
        public UnitConverter.SpeedUnits GetDefaultSpeedUnit() {
            return UnitConverter.SpeedUnits.kKmph;
        }

        @Override
        public UnitConverter.PressureUnits GetDefaultPressureUnit() {
            return UnitConverter.PressureUnits.kMmHg;
        }
    };



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
    public void MphToMpsTests()
    {
        assertEquals(null, UnitConverter.GetInstance().MphToMps(null));
        assertEquals(0.0, UnitConverter.GetInstance().MphToMps(0));
        assertEquals(1.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MphToMps(2.23694), 5));
        assertEquals(-25.67, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MphToMps(-57.42215), 5));
        assertEquals(45.6, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MphToMps(102.00429), 5));
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
    public void KmphToMpsTests()
    {
        assertEquals(null, UnitConverter.GetInstance().KmphToMps(null));
        assertEquals(0.0, UnitConverter.GetInstance().KmphToMps(0));
        assertEquals(1.0, UnitConverter.GetInstance().KmphToMps(3.6));
        assertEquals(-25.67, UnitConverter.GetInstance().KmphToMps(-92.412));
        assertEquals(45.6, UnitConverter.GetInstance().KmphToMps(164.16));
    }



    @Test
    public void MphToKmphTests()
    {
        assertEquals(null, UnitConverter.GetInstance().MphToKmph(null));
        assertEquals(0.0, UnitConverter.GetInstance().MphToKmph(0));
        assertEquals(3.60001, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MphToKmph(2.23694), 5));
        assertEquals(-92.41199, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MphToKmph(-57.42215), 5));
        assertEquals(164.15999, MathMethods.GetInstance().Round(UnitConverter.GetInstance().MphToKmph(102.00429), 5));
    }



    @Test
    public void KmphToMphTests()
    {
        assertEquals(null, UnitConverter.GetInstance().KmphToMph(null));
        assertEquals(0.0, UnitConverter.GetInstance().KmphToMph(0));
        assertEquals(2.23694, MathMethods.GetInstance().Round(UnitConverter.GetInstance().KmphToMph(3.60001), 5));
        assertEquals(-57.42215, MathMethods.GetInstance().Round(UnitConverter.GetInstance().KmphToMph(-92.41199), 5));
        assertEquals(102.00429, MathMethods.GetInstance().Round(UnitConverter.GetInstance().KmphToMph(164.15999), 5));
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
    public void GetTempInPreferredUnitsTests()
    {
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, metricUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, metricUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, imperialUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, imperialUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(null, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, metricUnits, null));
        assertEquals(0, UnitConverter.GetInstance().GetTempInPreferredUnits(0, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(32.0, UnitConverter.GetInstance().GetTempInPreferredUnits(0, metricUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(0, imperialUnits, null));
        assertEquals(-17.77778, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(0, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(0, UnitConverter.GetInstance().GetTempInPreferredUnits(0, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, metricUnits, null));
        assertEquals(1, UnitConverter.GetInstance().GetTempInPreferredUnits(1, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(33.8, UnitConverter.GetInstance().GetTempInPreferredUnits(1, metricUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(1, imperialUnits, null));
        assertEquals(-17.22222, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(1, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(1, UnitConverter.GetInstance().GetTempInPreferredUnits(1, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, metricUnits, null));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(-7.3, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, metricUnits, UnitConverter.TempUnits.kFahrenheit), 5));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, imperialUnits, null));
        assertEquals(-29.90741, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetTempInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, metricUnits, null));
        assertEquals(13, UnitConverter.GetInstance().GetTempInPreferredUnits(13, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(55.4, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(13, metricUnits, UnitConverter.TempUnits.kFahrenheit), 5));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(13, imperialUnits, null));
        assertEquals(-10.55556, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(13, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(13, UnitConverter.GetInstance().GetTempInPreferredUnits(13, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, metricUnits, null));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(104.3, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, metricUnits, UnitConverter.TempUnits.kFahrenheit), 5));
        assertEquals(null, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, imperialUnits, null));
        assertEquals(4.53704, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetTempInPreferredUnits(40.1666667, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
    }



    @Test
    public void GetSpeedInPreferredUnitsTests()
    {
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, metricUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, metricUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, metricUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, imperialUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, imperialUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, kmphUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, kmphUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, kmphUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(null, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, metricUnits, null));
        assertEquals(0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(0.0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, metricUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(0.0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, metricUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, imperialUnits, null));
        assertEquals(0.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(0.0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, kmphUnits, null));
        assertEquals(0.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(0.0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, kmphUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(0, UnitConverter.GetInstance().GetSpeedInPreferredUnits(0, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, metricUnits, null));
        assertEquals(1, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(2.23694, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(3.6, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, imperialUnits, null));
        assertEquals(0.44704, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(1, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(1.609344, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, kmphUnits, null));
        assertEquals(0.27778, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(0.62137, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(1, UnitConverter.GetInstance().GetSpeedInPreferredUnits(1, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, metricUnits, null));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(-48.83978, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(-78.6, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, imperialUnits, null));
        assertEquals(-9.76037, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(-35.13734, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, kmphUnits, null));
        assertEquals(-6.06481, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(-13.5666, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetSpeedInPreferredUnits(-21.8333333, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, metricUnits, null));
        assertEquals(13, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(29.08017, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(46.8, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, imperialUnits, null));
        assertEquals(5.81152, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(13, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(20.921472, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, kmphUnits, null));
        assertEquals(3.61111, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(8.07783, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(13, UnitConverter.GetInstance().GetSpeedInPreferredUnits(13, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, metricUnits, null));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(89.85027, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(144.6, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, imperialUnits, null));
        assertEquals(17.95611, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(64.64198, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, imperialUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, kmphUnits, null));
        assertEquals(11.15741, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(24.95841, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetSpeedInPreferredUnits(40.1666667, kmphUnits, UnitConverter.SpeedUnits.kKmph));
    }



    @Test
    public void GetPressureInPreferredUnitsTests()
    {
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, null, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, nullUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, metricUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, metricUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, metricUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, imperialUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, imperialUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(null, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, null, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, nullUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, metricUnits, null));
        assertEquals(0, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, metricUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(0.0, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, metricUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, imperialUnits, null));
        assertEquals(0.0, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(0, imperialUnits, UnitConverter.PressureUnits.kMmHg), 5));
        assertEquals(0, UnitConverter.GetInstance().GetPressureInPreferredUnits(0, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, null, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, nullUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, metricUnits, null));
        assertEquals(1, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, metricUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(0.03937, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(1, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, imperialUnits, null));
        assertEquals(25.4, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(1, imperialUnits, UnitConverter.PressureUnits.kMmHg), 5));
        assertEquals(1, UnitConverter.GetInstance().GetPressureInPreferredUnits(1, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, null, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, nullUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, metricUnits, null));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, metricUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(-0.85958, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, imperialUnits, null));
        assertEquals(-554.56667, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.PressureUnits.kMmHg), 5));
        assertEquals(-21.8333333, UnitConverter.GetInstance().GetPressureInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, null, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, nullUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, metricUnits, null));
        assertEquals(13, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, metricUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(0.51181, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(13, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, imperialUnits, null));
        assertEquals(330.2, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(13, imperialUnits, UnitConverter.PressureUnits.kMmHg), 5));
        assertEquals(13, UnitConverter.GetInstance().GetPressureInPreferredUnits(13, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, null, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, null, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, nullUnits, null));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, nullUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, metricUnits, null));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, metricUnits, UnitConverter.PressureUnits.kMmHg));
        assertEquals(1.58136, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, imperialUnits, null));
        assertEquals(1020.23333, MathMethods.GetInstance().Round(UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, imperialUnits, UnitConverter.PressureUnits.kMmHg), 5));
        assertEquals(40.1666667, UnitConverter.GetInstance().GetPressureInPreferredUnits(40.1666667, imperialUnits, UnitConverter.PressureUnits.kInHg));
    }



    @Test
    public void GetCompassDirectionsTest()
    {
        assertEquals(null, UnitConverter.GetInstance().GetCompassDirection(null));
        assertEquals(UnitConverter.CompassDirections.N, UnitConverter.GetInstance().GetCompassDirection(0));
        assertEquals(UnitConverter.CompassDirections.N, UnitConverter.GetInstance().GetCompassDirection(11.25));
        assertEquals(UnitConverter.CompassDirections.NNE, UnitConverter.GetInstance().GetCompassDirection(11.26));
        assertEquals(UnitConverter.CompassDirections.NNE, UnitConverter.GetInstance().GetCompassDirection(22.5));
        assertEquals(UnitConverter.CompassDirections.NNE, UnitConverter.GetInstance().GetCompassDirection(33.75));
        assertEquals(UnitConverter.CompassDirections.NE, UnitConverter.GetInstance().GetCompassDirection(33.76));
        assertEquals(UnitConverter.CompassDirections.NE, UnitConverter.GetInstance().GetCompassDirection(45));
        assertEquals(UnitConverter.CompassDirections.NE, UnitConverter.GetInstance().GetCompassDirection(56.25));
        assertEquals(UnitConverter.CompassDirections.ENE, UnitConverter.GetInstance().GetCompassDirection(56.26));
        assertEquals(UnitConverter.CompassDirections.ENE, UnitConverter.GetInstance().GetCompassDirection(67.5));
        assertEquals(UnitConverter.CompassDirections.ENE, UnitConverter.GetInstance().GetCompassDirection(78.75));
        assertEquals(UnitConverter.CompassDirections.E, UnitConverter.GetInstance().GetCompassDirection(78.76));
        assertEquals(UnitConverter.CompassDirections.E, UnitConverter.GetInstance().GetCompassDirection(90));
        assertEquals(UnitConverter.CompassDirections.E, UnitConverter.GetInstance().GetCompassDirection(101.25));
        assertEquals(UnitConverter.CompassDirections.ESE, UnitConverter.GetInstance().GetCompassDirection(101.26));
        assertEquals(UnitConverter.CompassDirections.ESE, UnitConverter.GetInstance().GetCompassDirection(112.5));
        assertEquals(UnitConverter.CompassDirections.ESE, UnitConverter.GetInstance().GetCompassDirection(123.75));
        assertEquals(UnitConverter.CompassDirections.SE, UnitConverter.GetInstance().GetCompassDirection(123.76));
        assertEquals(UnitConverter.CompassDirections.SE, UnitConverter.GetInstance().GetCompassDirection(135));
        assertEquals(UnitConverter.CompassDirections.SE, UnitConverter.GetInstance().GetCompassDirection(146.25));
        assertEquals(UnitConverter.CompassDirections.SSE, UnitConverter.GetInstance().GetCompassDirection(146.26));
        assertEquals(UnitConverter.CompassDirections.SSE, UnitConverter.GetInstance().GetCompassDirection(157.5));
        assertEquals(UnitConverter.CompassDirections.SSE, UnitConverter.GetInstance().GetCompassDirection(168.75));
        assertEquals(UnitConverter.CompassDirections.S, UnitConverter.GetInstance().GetCompassDirection(168.76));
        assertEquals(UnitConverter.CompassDirections.S, UnitConverter.GetInstance().GetCompassDirection(180));
        assertEquals(UnitConverter.CompassDirections.S, UnitConverter.GetInstance().GetCompassDirection(191.25));
        assertEquals(UnitConverter.CompassDirections.SSW, UnitConverter.GetInstance().GetCompassDirection(191.26));
        assertEquals(UnitConverter.CompassDirections.SSW, UnitConverter.GetInstance().GetCompassDirection(202.5));
        assertEquals(UnitConverter.CompassDirections.SSW, UnitConverter.GetInstance().GetCompassDirection(213.75));
        assertEquals(UnitConverter.CompassDirections.SW, UnitConverter.GetInstance().GetCompassDirection(213.76));
        assertEquals(UnitConverter.CompassDirections.SW, UnitConverter.GetInstance().GetCompassDirection(225));
        assertEquals(UnitConverter.CompassDirections.SW, UnitConverter.GetInstance().GetCompassDirection(236.25));
        assertEquals(UnitConverter.CompassDirections.WSW, UnitConverter.GetInstance().GetCompassDirection(236.26));
        assertEquals(UnitConverter.CompassDirections.WSW, UnitConverter.GetInstance().GetCompassDirection(247.5));
        assertEquals(UnitConverter.CompassDirections.WSW, UnitConverter.GetInstance().GetCompassDirection(258.75));
        assertEquals(UnitConverter.CompassDirections.W, UnitConverter.GetInstance().GetCompassDirection(258.76));
        assertEquals(UnitConverter.CompassDirections.W, UnitConverter.GetInstance().GetCompassDirection(270));
        assertEquals(UnitConverter.CompassDirections.W, UnitConverter.GetInstance().GetCompassDirection(281.25));
        assertEquals(UnitConverter.CompassDirections.WNW, UnitConverter.GetInstance().GetCompassDirection(281.26));
        assertEquals(UnitConverter.CompassDirections.WNW, UnitConverter.GetInstance().GetCompassDirection(292.5));
        assertEquals(UnitConverter.CompassDirections.WNW, UnitConverter.GetInstance().GetCompassDirection(303.75));
        assertEquals(UnitConverter.CompassDirections.NW, UnitConverter.GetInstance().GetCompassDirection(303.76));
        assertEquals(UnitConverter.CompassDirections.NW, UnitConverter.GetInstance().GetCompassDirection(315));
        assertEquals(UnitConverter.CompassDirections.NW, UnitConverter.GetInstance().GetCompassDirection(326.25));
        assertEquals(UnitConverter.CompassDirections.NNW, UnitConverter.GetInstance().GetCompassDirection(326.26));
        assertEquals(UnitConverter.CompassDirections.NNW, UnitConverter.GetInstance().GetCompassDirection(337.5));
        assertEquals(UnitConverter.CompassDirections.NNW, UnitConverter.GetInstance().GetCompassDirection(348.75));
        assertEquals(UnitConverter.CompassDirections.N, UnitConverter.GetInstance().GetCompassDirection(348.76));
        assertEquals(UnitConverter.CompassDirections.N, UnitConverter.GetInstance().GetCompassDirection(360));

        assertEquals(UnitConverter.CompassDirections.W, UnitConverter.GetInstance().GetCompassDirection(-90));
        assertEquals(UnitConverter.CompassDirections.SSE, UnitConverter.GetInstance().GetCompassDirection(-560));
        assertEquals(UnitConverter.CompassDirections.NNE, UnitConverter.GetInstance().GetCompassDirection(750));
        assertEquals(UnitConverter.CompassDirections.E, UnitConverter.GetInstance().GetCompassDirection(450));
    }
}