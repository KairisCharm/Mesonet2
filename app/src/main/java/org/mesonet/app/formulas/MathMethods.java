package org.mesonet.app.formulas;


import javax.inject.Inject;

public class MathMethods
{
    @Inject
    MathMethods(){}



    public Number Round(Number inBase, int inDecimals)
    {
        if(inBase == null)
            return null;

        double tenTimesDecimals = Math.pow(10, inDecimals);

        return Math.round(inBase.doubleValue() * tenTimesDecimals) / tenTimesDecimals;
    }
}
