package org.mesonet.app.formulas;



public class MathMethods
{
    private static MathMethods sMathMethods;



    public static MathMethods GetInstance()
    {
        if(sMathMethods == null)
            sMathMethods = new MathMethods();

        return sMathMethods;
    }



    private MathMethods(){}



    public Number Round(Number inBase, int inDecimals)
    {
        if(inBase == null)
            return null;

        double tenTimesDecimals = Math.pow(10, inDecimals);

        return Math.round(inBase.doubleValue() * tenTimesDecimals) / tenTimesDecimals;
    }
}
