package org.mesonet.app.mesonetdata;


import java.util.Locale;



public class MesonetUIController
{
    private MesonetDataController mDataController;



    public MesonetUIController(MesonetDataController inDataController)
    {
        mDataController = inDataController;
    }



    public String GetAirTempString()
    {
        if(mDataController == null)
            return "_";

        Double temp = mDataController.GetTemp();

        if(temp == null)
            return "_";

        return String.format(Locale.getDefault(),"%.0f", temp) + "°";
    }
}
