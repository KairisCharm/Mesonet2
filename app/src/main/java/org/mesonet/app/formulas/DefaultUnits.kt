package org.mesonet.app.formulas


interface DefaultUnits {
    fun GetDefaultTempUnit(): UnitConverter.TempUnits?
    fun GetDefaultLengthUnit(): UnitConverter.LengthUnits?
    fun GetDefaultSpeedUnit(): UnitConverter.SpeedUnits?
    fun GetDefaultPressureUnit(): UnitConverter.PressureUnits?
}
