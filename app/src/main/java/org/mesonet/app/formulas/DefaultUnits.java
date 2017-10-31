package org.mesonet.app.formulas;



public interface DefaultUnits
{
    UnitConverter.TempUnits GetDefaultTempUnit();
    UnitConverter.LengthUnits GetDefaultLengthUnit();
    UnitConverter.SpeedUnits GetDefaultSpeedUnit();
    UnitConverter.PressureUnits GetDefaultPressureUnit();
}
