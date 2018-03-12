package org.mesonet.app.formulas;


import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;

public class UnitConversionTests
{
    @Inject
    UnitConverter mUnitConverter;
    
    @Inject
    MathMethods mMathMethods;
    
    
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
            return UnitConverter.PressureUnits.kMb;
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
            return UnitConverter.PressureUnits.kMb;
        }
    };
    
    
    
    @Inject
    public UnitConversionTests(){}



    @Test
    public void CelsiusToFahrenheitTests()
    {
        assertEquals(null, mUnitConverter.CelsiusToFahrenheit(null));
        assertEquals(32.0, mUnitConverter.CelsiusToFahrenheit(0));
        assertEquals(33.8, mUnitConverter.CelsiusToFahrenheit(1));
        assertEquals(-7.3, mMathMethods.Round(mUnitConverter.CelsiusToFahrenheit(-21.8333333), 5));
        assertEquals(55.4, mMathMethods.Round(mUnitConverter.CelsiusToFahrenheit(13), 5));
        assertEquals(104.3, mMathMethods.Round(mUnitConverter.CelsiusToFahrenheit(40.1666667), 5));
    }



    @Test
    public void FahrenheitToCelsiusTests()
    {
        assertEquals(null, mUnitConverter.FahrenheitToCelsius(null));
        assertEquals(0.0, mUnitConverter.FahrenheitToCelsius(32.0));
        assertEquals(1.0, mMathMethods.Round(mUnitConverter.FahrenheitToCelsius(33.8), 5));
        assertEquals(-21.83333, mMathMethods.Round(mUnitConverter.FahrenheitToCelsius(-7.3), 5));
        assertEquals(13.0, mMathMethods.Round(mUnitConverter.FahrenheitToCelsius(55.4), 5));
        assertEquals(40.16667, mMathMethods.Round(mUnitConverter.FahrenheitToCelsius(104.3), 5));
    }



    @Test
    public void MpsToMphTests()
    {
        assertEquals(null, mUnitConverter.MpsToMph(null));
        assertEquals(0.0, mUnitConverter.MpsToMph(0));
        assertEquals(2.23694, mMathMethods.Round(mUnitConverter.MpsToMph(1), 5));
        assertEquals(-57.42215, mMathMethods.Round(mUnitConverter.MpsToMph(-25.67), 5));
        assertEquals(102.00429, mMathMethods.Round(mUnitConverter.MpsToMph(45.6), 5));
    }



    @Test
    public void MphToMpsTests()
    {
        assertEquals(null, mUnitConverter.MphToMps(null));
        assertEquals(0.0, mUnitConverter.MphToMps(0));
        assertEquals(1.0, mMathMethods.Round(mUnitConverter.MphToMps(2.23694), 5));
        assertEquals(-25.67, mMathMethods.Round(mUnitConverter.MphToMps(-57.42215), 5));
        assertEquals(45.6, mMathMethods.Round(mUnitConverter.MphToMps(102.00429), 5));
    }



    @Test
    public void MpsToKmphTests()
    {
        assertEquals(null, mUnitConverter.MpsToKmph(null));
        assertEquals(0.0, mUnitConverter.MpsToKmph(0));
        assertEquals(3.6, mUnitConverter.MpsToKmph(1));
        assertEquals(-92.412, mUnitConverter.MpsToKmph(-25.67));
        assertEquals(164.16, mUnitConverter.MpsToKmph(45.6));
    }



    @Test
    public void KmphToMpsTests()
    {
        assertEquals(null, mUnitConverter.KmphToMps(null));
        assertEquals(0.0, mUnitConverter.KmphToMps(0));
        assertEquals(1.0, mUnitConverter.KmphToMps(3.6));
        assertEquals(-25.67, mUnitConverter.KmphToMps(-92.412));
        assertEquals(45.6, mUnitConverter.KmphToMps(164.16));
    }



    @Test
    public void MphToKmphTests()
    {
        assertEquals(null, mUnitConverter.MphToKmph(null));
        assertEquals(0.0, mUnitConverter.MphToKmph(0));
        assertEquals(3.60001, mMathMethods.Round(mUnitConverter.MphToKmph(2.23694), 5));
        assertEquals(-92.41199, mMathMethods.Round(mUnitConverter.MphToKmph(-57.42215), 5));
        assertEquals(164.15999, mMathMethods.Round(mUnitConverter.MphToKmph(102.00429), 5));
    }



    @Test
    public void KmphToMphTests()
    {
        assertEquals(null, mUnitConverter.KmphToMph(null));
        assertEquals(0.0, mUnitConverter.KmphToMph(0));
        assertEquals(2.23694, mMathMethods.Round(mUnitConverter.KmphToMph(3.60001), 5));
        assertEquals(-57.42215, mMathMethods.Round(mUnitConverter.KmphToMph(-92.41199), 5));
        assertEquals(102.00429, mMathMethods.Round(mUnitConverter.KmphToMph(164.15999), 5));
    }



    @Test
    public void MmToInTests()
    {
        assertEquals(null, mUnitConverter.MmToIn(null));
        assertEquals(0.0, mUnitConverter.MmToIn(0));
        assertEquals(0.03937, mMathMethods.Round(mUnitConverter.MmToIn(1), 5));
        assertEquals(-1.01063, mMathMethods.Round(mUnitConverter.MmToIn(-25.67), 5));
        assertEquals(1.79528, mMathMethods.Round(mUnitConverter.MmToIn(45.6), 5));
    }



    @Test
    public void GetTempInPreferredUnitsTests()
    {
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, null, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, nullUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, metricUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, metricUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, imperialUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, imperialUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(null, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, null, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, nullUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, metricUnits, null));
        assertEquals(0, mUnitConverter.GetTempInPreferredUnits(0, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(32.0, mUnitConverter.GetTempInPreferredUnits(0, metricUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(0, imperialUnits, null));
        assertEquals(-17.77778, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(0, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(0, mUnitConverter.GetTempInPreferredUnits(0, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, null, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, nullUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, metricUnits, null));
        assertEquals(1, mUnitConverter.GetTempInPreferredUnits(1, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(33.8, mUnitConverter.GetTempInPreferredUnits(1, metricUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(1, imperialUnits, null));
        assertEquals(-17.22222, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(1, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(1, mUnitConverter.GetTempInPreferredUnits(1, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, null, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, nullUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, metricUnits, null));
        assertEquals(-21.8333333, mUnitConverter.GetTempInPreferredUnits(-21.8333333, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(-7.3, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(-21.8333333, metricUnits, UnitConverter.TempUnits.kFahrenheit), 5));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(-21.8333333, imperialUnits, null));
        assertEquals(-29.90741, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(-21.8333333, mUnitConverter.GetTempInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, null, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, nullUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, metricUnits, null));
        assertEquals(13, mUnitConverter.GetTempInPreferredUnits(13, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(55.4, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(13, metricUnits, UnitConverter.TempUnits.kFahrenheit), 5));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(13, imperialUnits, null));
        assertEquals(-10.55556, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(13, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(13, mUnitConverter.GetTempInPreferredUnits(13, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, null, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, null, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, null, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, nullUnits, null));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, nullUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, nullUnits, UnitConverter.TempUnits.kFahrenheit));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, metricUnits, null));
        assertEquals(40.1666667, mUnitConverter.GetTempInPreferredUnits(40.1666667, metricUnits, UnitConverter.TempUnits.kCelsius));
        assertEquals(104.3, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(40.1666667, metricUnits, UnitConverter.TempUnits.kFahrenheit), 5));
        assertEquals(null, mUnitConverter.GetTempInPreferredUnits(40.1666667, imperialUnits, null));
        assertEquals(4.53704, mMathMethods.Round(mUnitConverter.GetTempInPreferredUnits(40.1666667, imperialUnits, UnitConverter.TempUnits.kCelsius), 5));
        assertEquals(40.1666667, mUnitConverter.GetTempInPreferredUnits(40.1666667, imperialUnits, UnitConverter.TempUnits.kFahrenheit));
    }



    @Test
    public void GetSpeedInPreferredUnitsTests()
    {
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, null, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, nullUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, metricUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, metricUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, metricUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, imperialUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, imperialUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, kmphUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, kmphUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, kmphUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(null, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, null, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, nullUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, metricUnits, null));
        assertEquals(0, mUnitConverter.GetSpeedInPreferredUnits(0, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(0.0, mUnitConverter.GetSpeedInPreferredUnits(0, metricUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(0.0, mUnitConverter.GetSpeedInPreferredUnits(0, metricUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, imperialUnits, null));
        assertEquals(0.0, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(0, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(0, mUnitConverter.GetSpeedInPreferredUnits(0, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(0.0, mUnitConverter.GetSpeedInPreferredUnits(0, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(0, kmphUnits, null));
        assertEquals(0.0, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(0, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(0.0, mUnitConverter.GetSpeedInPreferredUnits(0, kmphUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(0, mUnitConverter.GetSpeedInPreferredUnits(0, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, null, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, nullUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, metricUnits, null));
        assertEquals(1, mUnitConverter.GetSpeedInPreferredUnits(1, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(2.23694, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(1, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(3.6, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(1, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, imperialUnits, null));
        assertEquals(0.44704, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(1, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(1, mUnitConverter.GetSpeedInPreferredUnits(1, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(1.609344, mUnitConverter.GetSpeedInPreferredUnits(1, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(1, kmphUnits, null));
        assertEquals(0.27778, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(1, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(0.62137, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(1, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(1, mUnitConverter.GetSpeedInPreferredUnits(1, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, null, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, nullUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, metricUnits, null));
        assertEquals(-21.8333333, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(-48.83978, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(-78.6, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, imperialUnits, null));
        assertEquals(-9.76037, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(-21.8333333, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(-35.13734, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, kmphUnits, null));
        assertEquals(-6.06481, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(-13.5666, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(-21.8333333, mUnitConverter.GetSpeedInPreferredUnits(-21.8333333, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, null, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, nullUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, metricUnits, null));
        assertEquals(13, mUnitConverter.GetSpeedInPreferredUnits(13, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(29.08017, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(13, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(46.8, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(13, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, imperialUnits, null));
        assertEquals(5.81152, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(13, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(13, mUnitConverter.GetSpeedInPreferredUnits(13, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(20.921472, mUnitConverter.GetSpeedInPreferredUnits(13, imperialUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(13, kmphUnits, null));
        assertEquals(3.61111, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(13, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(8.07783, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(13, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(13, mUnitConverter.GetSpeedInPreferredUnits(13, kmphUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, null, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, null, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, null, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, null, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, nullUnits, null));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, nullUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, nullUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, nullUnits, UnitConverter.SpeedUnits.kKmph));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, metricUnits, null));
        assertEquals(40.1666667, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, metricUnits, UnitConverter.SpeedUnits.kMps));
        assertEquals(89.85027, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(40.1666667, metricUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(144.6, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(40.1666667, metricUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, imperialUnits, null));
        assertEquals(17.95611, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(40.1666667, imperialUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(40.1666667, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, imperialUnits, UnitConverter.SpeedUnits.kMph));
        assertEquals(64.64198, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(40.1666667, imperialUnits, UnitConverter.SpeedUnits.kKmph), 5));
        assertEquals(null, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, kmphUnits, null));
        assertEquals(11.15741, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(40.1666667, kmphUnits, UnitConverter.SpeedUnits.kMps), 5));
        assertEquals(24.95841, mMathMethods.Round(mUnitConverter.GetSpeedInPreferredUnits(40.1666667, kmphUnits, UnitConverter.SpeedUnits.kMph), 5));
        assertEquals(40.1666667, mUnitConverter.GetSpeedInPreferredUnits(40.1666667, kmphUnits, UnitConverter.SpeedUnits.kKmph));
    }



    @Test
    public void GetPressureInPreferredUnitsTests()
    {
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, null, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, null, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, nullUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, nullUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, metricUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, metricUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, metricUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, imperialUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, imperialUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(null, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, null, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, null, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, nullUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, nullUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, metricUnits, null));
        assertEquals(0, mUnitConverter.GetPressureInPreferredUnits(0, metricUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(0.0, mUnitConverter.GetPressureInPreferredUnits(0, metricUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(0, imperialUnits, null));
        assertEquals(0.0, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(0, imperialUnits, UnitConverter.PressureUnits.kMb), 5));
        assertEquals(0, mUnitConverter.GetPressureInPreferredUnits(0, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, null, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, null, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, nullUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, nullUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, metricUnits, null));
        assertEquals(1, mUnitConverter.GetPressureInPreferredUnits(1, metricUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(0.03937, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(1, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(1, imperialUnits, null));
        assertEquals(25.4, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(1, imperialUnits, UnitConverter.PressureUnits.kMb), 5));
        assertEquals(1, mUnitConverter.GetPressureInPreferredUnits(1, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, null, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, null, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, nullUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, nullUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, metricUnits, null));
        assertEquals(-21.8333333, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, metricUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(-0.85958, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(-21.8333333, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, imperialUnits, null));
        assertEquals(-554.56667, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.PressureUnits.kMb), 5));
        assertEquals(-21.8333333, mUnitConverter.GetPressureInPreferredUnits(-21.8333333, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, null, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, null, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, nullUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, nullUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, metricUnits, null));
        assertEquals(13, mUnitConverter.GetPressureInPreferredUnits(13, metricUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(0.51181, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(13, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(13, imperialUnits, null));
        assertEquals(330.2, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(13, imperialUnits, UnitConverter.PressureUnits.kMb), 5));
        assertEquals(13, mUnitConverter.GetPressureInPreferredUnits(13, imperialUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, null, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, null, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, null, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, nullUnits, null));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, nullUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, nullUnits, UnitConverter.PressureUnits.kInHg));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, metricUnits, null));
        assertEquals(40.1666667, mUnitConverter.GetPressureInPreferredUnits(40.1666667, metricUnits, UnitConverter.PressureUnits.kMb));
        assertEquals(1.58136, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(40.1666667, metricUnits, UnitConverter.PressureUnits.kInHg), 5));
        assertEquals(null, mUnitConverter.GetPressureInPreferredUnits(40.1666667, imperialUnits, null));
        assertEquals(1020.23333, mMathMethods.Round(mUnitConverter.GetPressureInPreferredUnits(40.1666667, imperialUnits, UnitConverter.PressureUnits.kMb), 5));
        assertEquals(40.1666667, mUnitConverter.GetPressureInPreferredUnits(40.1666667, imperialUnits, UnitConverter.PressureUnits.kInHg));
    }



    @Test
    public void GetCompassDirectionsTest()
    {
        assertEquals(null, mUnitConverter.GetCompassDirection(null));
        assertEquals(UnitConverter.CompassDirections.N, mUnitConverter.GetCompassDirection(0));
        assertEquals(UnitConverter.CompassDirections.N, mUnitConverter.GetCompassDirection(11.25));
        assertEquals(UnitConverter.CompassDirections.NNE, mUnitConverter.GetCompassDirection(11.26));
        assertEquals(UnitConverter.CompassDirections.NNE, mUnitConverter.GetCompassDirection(22.5));
        assertEquals(UnitConverter.CompassDirections.NNE, mUnitConverter.GetCompassDirection(33.75));
        assertEquals(UnitConverter.CompassDirections.NE, mUnitConverter.GetCompassDirection(33.76));
        assertEquals(UnitConverter.CompassDirections.NE, mUnitConverter.GetCompassDirection(45));
        assertEquals(UnitConverter.CompassDirections.NE, mUnitConverter.GetCompassDirection(56.25));
        assertEquals(UnitConverter.CompassDirections.ENE, mUnitConverter.GetCompassDirection(56.26));
        assertEquals(UnitConverter.CompassDirections.ENE, mUnitConverter.GetCompassDirection(67.5));
        assertEquals(UnitConverter.CompassDirections.ENE, mUnitConverter.GetCompassDirection(78.75));
        assertEquals(UnitConverter.CompassDirections.E, mUnitConverter.GetCompassDirection(78.76));
        assertEquals(UnitConverter.CompassDirections.E, mUnitConverter.GetCompassDirection(90));
        assertEquals(UnitConverter.CompassDirections.E, mUnitConverter.GetCompassDirection(101.25));
        assertEquals(UnitConverter.CompassDirections.ESE, mUnitConverter.GetCompassDirection(101.26));
        assertEquals(UnitConverter.CompassDirections.ESE, mUnitConverter.GetCompassDirection(112.5));
        assertEquals(UnitConverter.CompassDirections.ESE, mUnitConverter.GetCompassDirection(123.75));
        assertEquals(UnitConverter.CompassDirections.SE, mUnitConverter.GetCompassDirection(123.76));
        assertEquals(UnitConverter.CompassDirections.SE, mUnitConverter.GetCompassDirection(135));
        assertEquals(UnitConverter.CompassDirections.SE, mUnitConverter.GetCompassDirection(146.25));
        assertEquals(UnitConverter.CompassDirections.SSE, mUnitConverter.GetCompassDirection(146.26));
        assertEquals(UnitConverter.CompassDirections.SSE, mUnitConverter.GetCompassDirection(157.5));
        assertEquals(UnitConverter.CompassDirections.SSE, mUnitConverter.GetCompassDirection(168.75));
        assertEquals(UnitConverter.CompassDirections.S, mUnitConverter.GetCompassDirection(168.76));
        assertEquals(UnitConverter.CompassDirections.S, mUnitConverter.GetCompassDirection(180));
        assertEquals(UnitConverter.CompassDirections.S, mUnitConverter.GetCompassDirection(191.25));
        assertEquals(UnitConverter.CompassDirections.SSW, mUnitConverter.GetCompassDirection(191.26));
        assertEquals(UnitConverter.CompassDirections.SSW, mUnitConverter.GetCompassDirection(202.5));
        assertEquals(UnitConverter.CompassDirections.SSW, mUnitConverter.GetCompassDirection(213.75));
        assertEquals(UnitConverter.CompassDirections.SW, mUnitConverter.GetCompassDirection(213.76));
        assertEquals(UnitConverter.CompassDirections.SW, mUnitConverter.GetCompassDirection(225));
        assertEquals(UnitConverter.CompassDirections.SW, mUnitConverter.GetCompassDirection(236.25));
        assertEquals(UnitConverter.CompassDirections.WSW, mUnitConverter.GetCompassDirection(236.26));
        assertEquals(UnitConverter.CompassDirections.WSW, mUnitConverter.GetCompassDirection(247.5));
        assertEquals(UnitConverter.CompassDirections.WSW, mUnitConverter.GetCompassDirection(258.75));
        assertEquals(UnitConverter.CompassDirections.W, mUnitConverter.GetCompassDirection(258.76));
        assertEquals(UnitConverter.CompassDirections.W, mUnitConverter.GetCompassDirection(270));
        assertEquals(UnitConverter.CompassDirections.W, mUnitConverter.GetCompassDirection(281.25));
        assertEquals(UnitConverter.CompassDirections.WNW, mUnitConverter.GetCompassDirection(281.26));
        assertEquals(UnitConverter.CompassDirections.WNW, mUnitConverter.GetCompassDirection(292.5));
        assertEquals(UnitConverter.CompassDirections.WNW, mUnitConverter.GetCompassDirection(303.75));
        assertEquals(UnitConverter.CompassDirections.NW, mUnitConverter.GetCompassDirection(303.76));
        assertEquals(UnitConverter.CompassDirections.NW, mUnitConverter.GetCompassDirection(315));
        assertEquals(UnitConverter.CompassDirections.NW, mUnitConverter.GetCompassDirection(326.25));
        assertEquals(UnitConverter.CompassDirections.NNW, mUnitConverter.GetCompassDirection(326.26));
        assertEquals(UnitConverter.CompassDirections.NNW, mUnitConverter.GetCompassDirection(337.5));
        assertEquals(UnitConverter.CompassDirections.NNW, mUnitConverter.GetCompassDirection(348.75));
        assertEquals(UnitConverter.CompassDirections.N, mUnitConverter.GetCompassDirection(348.76));
        assertEquals(UnitConverter.CompassDirections.N, mUnitConverter.GetCompassDirection(360));

        assertEquals(UnitConverter.CompassDirections.W, mUnitConverter.GetCompassDirection(-90));
        assertEquals(UnitConverter.CompassDirections.SSE, mUnitConverter.GetCompassDirection(-560));
        assertEquals(UnitConverter.CompassDirections.NNE, mUnitConverter.GetCompassDirection(750));
        assertEquals(UnitConverter.CompassDirections.E, mUnitConverter.GetCompassDirection(450));
    }
}