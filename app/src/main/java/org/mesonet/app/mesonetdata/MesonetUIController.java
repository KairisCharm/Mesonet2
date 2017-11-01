package org.mesonet.app.mesonetdata;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;



public class MesonetUIController extends Observable implements Observer
{
    private MesonetDataController mDataController;



    public MesonetUIController(MesonetDataController inDataController)
    {
        mDataController = inDataController;
        mDataController.addObserver(this);
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

    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers();
    }
}
