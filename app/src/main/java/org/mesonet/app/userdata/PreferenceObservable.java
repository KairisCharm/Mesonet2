package org.mesonet.app.userdata;

import java.util.Observable;



public class PreferenceObservable extends Observable
{
    @Override
    public void notifyObservers()
    {
        setChanged();
        super.notifyObservers();
    }
}
