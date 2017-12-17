package org.mesonet.app.userdata;

import java.util.Observable;



public class PreferencesObservable extends Observable
{
    public PreferencesObservable(){}



    @Override
    public void notifyObservers()
    {
        setChanged();
        super.notifyObservers();
    }
}
